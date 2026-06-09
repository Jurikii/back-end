package com.juriki.api_juriki.advogado.repository;

import com.juriki.api_juriki.advogado.model.Advogado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Integer> {

    // Verifica se já existe advogado com essa OAB + estado (constraint uk_oab_estado)
    boolean existsByOabAndEstado(String oab, String estado);

    // Verifica se o usuário já é advogado cadastrado
    boolean existsByUsuarioId(Integer idUsuario);
    
    boolean existsByUsuarioIdAndUsuarioDeletadoEmIsNull(Integer idUsuario);

    // Busca advogados já aprovados pela plataforma
    List<Advogado> findByAprovadoTrue();

    // Busca por especialidade (útil para sugestão de advogados)
    List<Advogado> findByEspecialidadeContainingIgnoreCaseAndAprovadoTrue(String especialidade);

    Optional<Advogado> findByUsuarioId(Integer idUsuario);
    
    Optional<Advogado> findByUsuarioIdAndUsuarioDeletadoEmIsNull(Integer idUsuario);
    
    List<Advogado> findAllByUsuarioDeletadoEmIsNull();
    
    Optional<Advogado> findByIdAndUsuarioDeletadoEmIsNull(Integer idAdvogado);
}
