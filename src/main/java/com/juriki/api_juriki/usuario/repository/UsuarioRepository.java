package com.juriki.api_juriki.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juriki.api_juriki.usuario.enums.EStatusConta;
import com.juriki.api_juriki.usuario.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    // Busca apenas usuários não deletados
    Optional<Usuario> findByIdAndDeletadoEmIsNull(Integer id);

    // ========== Consultas para usuários ativos ==========

    List<Usuario> findAllByStatusContaAndDeletadoEmIsNull(EStatusConta status);

    Optional<Usuario> findByIdAndStatusContaAndDeletadoEmIsNull(Integer id, EStatusConta status);
}