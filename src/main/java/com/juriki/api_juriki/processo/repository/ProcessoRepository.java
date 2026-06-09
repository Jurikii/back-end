package com.juriki.api_juriki.processo.repository;

import com.juriki.api_juriki.processo.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

    List<Processo> findByUsuarioIdOrderByDataAtualizacaoDesc(Integer idUsuario);

    List<Processo> findByUsuarioIdAndStatusProcessoOrderByDataAtualizacaoDesc(
            Integer idUsuario, String statusProcesso
    );

    Optional<Processo> findByIdAndUsuarioId(Integer idProcesso, Integer idUsuario);

    Optional<Processo> findByNumeroProcesso(String numeroProcesso);

    boolean existsByNumeroProcesso(String numeroProcesso);

    @Query("""
            SELECT DISTINCT p FROM Processo p
            LEFT JOIN FETCH p.advogados pa
            LEFT JOIN FETCH pa.advogado
            WHERE p.id = :idProcesso
            AND p.usuario.id = :idUsuario
            """)
    Optional<Processo> findByIdWithAdvogados(
            @Param("idProcesso") Integer idProcesso,
            @Param("idUsuario") Integer idUsuario
    );

    @Query("""
            SELECT p FROM Processo p
            JOIN p.advogados pa
            WHERE pa.advogado.id = :idAdvogado
            AND pa.ativo = true
            ORDER BY p.dataAtualizacao DESC
            """)
    List<Processo> findByAdvogadoAtivo(@Param("idAdvogado") int idAdvogado);

    @Query("""
            SELECT COUNT(pa) > 0 FROM ProcessoAdvogado pa
            WHERE pa.processo.id = :idProcesso
            AND pa.advogado.id = :idAdvogado
            AND pa.ativo = true
            """)
    boolean advogadoJaVinculado(
            @Param("idProcesso") Integer idProcesso,
            @Param("idAdvogado") Integer idAdvogado
    );
}
