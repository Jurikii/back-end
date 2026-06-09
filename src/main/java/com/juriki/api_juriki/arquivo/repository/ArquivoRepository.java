package com.juriki.api_juriki.arquivo.repository;

import com.juriki.api_juriki.arquivo.enums.OrigemArquivo;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import com.juriki.api_juriki.arquivo.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

    List<Arquivo> findByUsuarioIdOrderByDataUploadDesc(Integer idUsuario);

    List<Arquivo> findByUsuarioIdAndTipoArquivoOrderByDataUploadDesc(Integer idUsuario, TipoArquivo tipo);

    List<Arquivo> findByUsuarioIdAndOrigemOrderByDataUploadDesc(Integer idUsuario, OrigemArquivo origem);

    List<Arquivo> findByProcessoIdOrderByDataUploadDesc(Integer idProcesso);

    List<Arquivo> findByIdChatbotOrderByDataUploadDesc(Integer idChatbot);

    @Query("""
            SELECT COALESCE(SUM(a.tamanho), 0) FROM Arquivo a
            WHERE a.usuario.id = :idUsuario
            """)
    Long somarTamanhoTotalPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("""
            SELECT COUNT(a) FROM Arquivo a
            WHERE a.usuario.id = :idUsuario
            AND a.origem = :origem
            """)
    long countByUsuarioAndOrigem(
            @Param("idUsuario") Integer idUsuario,
            @Param("origem") OrigemArquivo origem
    );

    boolean existsByIdAndUsuarioId(Integer idArquivo, Integer idUsuario);
}
