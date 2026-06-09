package com.juriki.api_juriki.sessaousuario.repository;

import com.juriki.api_juriki.sessaousuario.model.SessaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SessaoUsuarioRepository extends JpaRepository<SessaoUsuario, Integer> {

    List<SessaoUsuario> findByUsuarioIdOrderByCriadoEmDesc(Integer idUsuario);

    Optional<SessaoUsuario> findByToken(String token);

    @Transactional
    void deleteByToken(String token);
}
