package com.juriki.api_juriki.perfil_ia.repository;

import com.juriki.api_juriki.perfil_ia.model.PerfilIaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilIaUsuarioRepository extends JpaRepository<PerfilIaUsuario, Integer> {

    List<PerfilIaUsuario> findByUsuarioId(Integer idUsuario);
}
