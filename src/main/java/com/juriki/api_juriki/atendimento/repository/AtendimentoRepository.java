package com.juriki.api_juriki.atendimento.repository;

import com.juriki.api_juriki.atendimento.enums.StatusAtendimento;
import com.juriki.api_juriki.atendimento.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

    List<Atendimento> findByUsuarioIdOrderByDataConsultaDesc(Integer idUsuario);

    List<Atendimento> findByAdvogadoIdOrderByDataConsultaDesc(Integer idAdvogado);

    List<Atendimento> findByUsuarioIdAndStatus(Integer idUsuario, StatusAtendimento status);

    List<Atendimento> findByAdvogadoIdAndStatus(Integer idAdvogado, StatusAtendimento status);

    @Query("""
            SELECT COUNT(a) > 0 FROM Atendimento a
            WHERE a.advogado.id = :idAdvogado
            AND a.status NOT IN ('CANCELADO', 'CONCLUIDO')
            AND a.dataConsulta BETWEEN :inicio AND :fim
            """)
    boolean existeConflito(
            @Param("idAdvogado") Integer idAdvogado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            SELECT a FROM Atendimento a
            WHERE a.usuario.id = :idUsuario
            AND a.dataConsulta > :agora
            AND a.status NOT IN ('CANCELADO', 'CONCLUIDO')
            ORDER BY a.dataConsulta ASC
            LIMIT 1
            """)
    Optional<Atendimento> findProximoAtendimentoUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("agora") LocalDateTime agora
    );

    boolean existsByUsuarioIdAndAdvogadoId(Integer idUsuario, Integer idAdvogado);

    @Query("""
            SELECT COUNT(a) FROM Atendimento a
            WHERE a.advogado.id = :idAdvogado
            AND a.dataConsulta BETWEEN :inicio AND :fim
            """)
    long countByAdvogadoAndPeriodo(
            @Param("idAdvogado") Integer idAdvogado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
