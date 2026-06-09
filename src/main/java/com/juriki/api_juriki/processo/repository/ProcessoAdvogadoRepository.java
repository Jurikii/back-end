package com.juriki.api_juriki.processo.repository;

import com.juriki.api_juriki.processo.model.ProcessoAdvogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessoAdvogadoRepository extends JpaRepository<ProcessoAdvogado, Integer> {

    // Busca vínculo ativo entre processo e advogado (usado na troca — RN12)
    @Query("""
            SELECT pa FROM ProcessoAdvogado pa
            WHERE pa.processo.id = :idProcesso
            AND pa.advogado.id = :idAdvogado
            AND pa.ativo = true
            """)
    Optional<ProcessoAdvogado> findVinculoAtivo(
            @Param("idProcesso") int idProcesso,
            @Param("idAdvogado") int idAdvogado
    );

    // Lista todos os vínculos ativos de um processo
    List<ProcessoAdvogado> findByProcessoIdAndAtivoTrue(int idProcesso);

    // Conta advogados ativos em um processo
    int countByProcessoIdAndAtivoTrue(Integer idProcesso);
}
