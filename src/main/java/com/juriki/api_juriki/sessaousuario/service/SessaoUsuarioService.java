package com.juriki.api_juriki.sessaousuario.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioAtualizarRequestDTO;
import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioCriarRequestDTO;
import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioResponseDTO;
import com.juriki.api_juriki.sessaousuario.mapper.SessaoUsuarioMapper;
import com.juriki.api_juriki.sessaousuario.model.SessaoUsuario;
import com.juriki.api_juriki.sessaousuario.repository.SessaoUsuarioRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoUsuarioService {

    private final SessaoUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoUsuarioMapper mapper;

    @Transactional
    public SessaoUsuarioResponseDTO criar(SessaoUsuarioCriarRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + dto.idUsuario()));

        SessaoUsuario sessao = SessaoUsuario.builder()
                .usuario(usuario)
                .token(dto.token())
                .ip(dto.ip())
                .userAgent(dto.userAgent())
                .expiraEm(dto.expiraEm())
                .build();

        sessao = repository.save(sessao);
        log.info("Sessão criada: ID {}", sessao.getId());
        return mapper.toResponseDTO(sessao);
    }

    @Transactional(readOnly = true)
    public List<SessaoUsuarioResponseDTO> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SessaoUsuarioResponseDTO> listarPorUsuario(Integer idUsuario) {
        return repository.findByUsuarioIdOrderByCriadoEmDesc(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public SessaoUsuarioResponseDTO buscarPorId(Integer id) {
        SessaoUsuario sessao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + id));
        return mapper.toResponseDTO(sessao);
    }

    @Transactional(readOnly = true)
    public SessaoUsuarioResponseDTO buscarPorToken(String token) {
        SessaoUsuario sessao = repository.findByToken(token)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada para o token informado"));
        return mapper.toResponseDTO(sessao);
    }

    @Transactional
    public SessaoUsuarioResponseDTO atualizar(Integer id, SessaoUsuarioAtualizarRequestDTO dto) {
        SessaoUsuario sessao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + id));

        if (dto.token() != null) sessao.setToken(dto.token());
        if (dto.ip() != null) sessao.setIp(dto.ip());
        if (dto.userAgent() != null) sessao.setUserAgent(dto.userAgent());
        if (dto.expiraEm() != null) sessao.setExpiraEm(dto.expiraEm());

        sessao = repository.save(sessao);
        log.info("Sessão atualizada: ID {}", sessao.getId());
        return mapper.toResponseDTO(sessao);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sessão não encontrada: " + id);
        }
        repository.deleteById(id);
        log.info("Sessão deletada: ID {}", id);
    }
}
