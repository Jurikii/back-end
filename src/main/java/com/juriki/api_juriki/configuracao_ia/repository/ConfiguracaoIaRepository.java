package com.juriki.api_juriki.configuracao_ia.repository;

import com.juriki.api_juriki.configuracao_ia.model.ConfiguracaoIa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoIaRepository extends JpaRepository<ConfiguracaoIa, Integer> {

    Optional<ConfiguracaoIa> findByUsuarioId(Integer idUsuario);
}
