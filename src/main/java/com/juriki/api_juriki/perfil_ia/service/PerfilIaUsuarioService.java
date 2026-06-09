package com.juriki.api_juriki.perfil_ia.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioAtualizarRequestDTO;
import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioCriarRequestDTO;
import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioResponseDTO;
import com.juriki.api_juriki.perfil_ia.model.PerfilIaUsuario;
import com.juriki.api_juriki.perfil_ia.repository.PerfilIaUsuarioRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerfilIaUsuarioService {

    private final PerfilIaUsuarioRepository perfilIaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PerfilIaUsuarioResponseDTO criar(PerfilIaUsuarioCriarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        PerfilIaUsuario perfil = PerfilIaUsuario.builder()
                .usuario(usuario)
                .nivelJuridico(request.getNivelJuridico())
                .estiloResposta(request.getEstiloResposta())
                .objetivoUso(request.getObjetivoUso())
                .areasInteresse(request.getAreasInteresse())
                .linguagemPreferida(request.getLinguagemPreferida())
                .build();

        perfilIaUsuarioRepository.save(perfil);

        log.info("Perfil IA criado para usuário ID {}", usuario.getId());

        return toResponse(perfil);
    }

    @Transactional(readOnly = true)
    public PerfilIaUsuarioResponseDTO buscarPorId(Integer idPerfil) {
        return toResponse(buscarPerfil(idPerfil));
    }

    @Transactional(readOnly = true)
    public List<PerfilIaUsuarioResponseDTO> listarTodos() {
        return perfilIaUsuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PerfilIaUsuarioResponseDTO> listarPorUsuario(Integer idUsuario) {
        return perfilIaUsuarioRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PerfilIaUsuarioResponseDTO atualizar(Integer idPerfil, PerfilIaUsuarioAtualizarRequestDTO request) {
        PerfilIaUsuario perfil = buscarPerfil(idPerfil);

        if (request.getNivelJuridico() != null) {
            perfil.setNivelJuridico(request.getNivelJuridico());
        }
        if (request.getEstiloResposta() != null) {
            perfil.setEstiloResposta(request.getEstiloResposta());
        }
        if (request.getObjetivoUso() != null) {
            perfil.setObjetivoUso(request.getObjetivoUso());
        }
        if (request.getAreasInteresse() != null) {
            perfil.setAreasInteresse(request.getAreasInteresse());
        }
        if (request.getLinguagemPreferida() != null) {
            perfil.setLinguagemPreferida(request.getLinguagemPreferida());
        }

        perfilIaUsuarioRepository.save(perfil);

        log.info("Perfil IA ID {} atualizado.", idPerfil);

        return toResponse(perfil);
    }

    @Transactional
    public void deletar(Integer idPerfil) {
        PerfilIaUsuario perfil = buscarPerfil(idPerfil);
        perfilIaUsuarioRepository.delete(perfil);
        log.info("Perfil IA ID {} removido.", idPerfil);
    }

    private PerfilIaUsuario buscarPerfil(Integer idPerfil) {
        return perfilIaUsuarioRepository.findById(idPerfil)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil IA não encontrado."));
    }

    private PerfilIaUsuarioResponseDTO toResponse(PerfilIaUsuario perfil) {
        return PerfilIaUsuarioResponseDTO.builder()
                .idPerfil(perfil.getId())
                .idUsuario(perfil.getUsuario().getId())
                .nivelJuridico(perfil.getNivelJuridico())
                .estiloResposta(perfil.getEstiloResposta())
                .objetivoUso(perfil.getObjetivoUso())
                .areasInteresse(perfil.getAreasInteresse())
                .linguagemPreferida(perfil.getLinguagemPreferida())
                .criadoEm(perfil.getCriadoEm())
                .atualizadoEm(perfil.getAtualizadoEm())
                .build();
    }
}
