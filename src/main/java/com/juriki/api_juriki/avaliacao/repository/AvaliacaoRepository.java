package com.juriki.api_juriki.avaliacao.repository;

import com.juriki.api_juriki.avaliacao.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    List<Avaliacao> findByUsuarioId(Integer idUsuario);

    List<Avaliacao> findByAdvogadoId(Integer idAdvogado);
}
