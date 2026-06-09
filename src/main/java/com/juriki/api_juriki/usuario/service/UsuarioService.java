package com.juriki.api_juriki.usuario.service;

import com.juriki.api_juriki.exception.ConflitoException;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.plano.model.Plano;
import com.juriki.api_juriki.plano.repository.PlanoRepository;
import com.juriki.api_juriki.usuario.dto.UsuarioAtualizarRequestDTO;
import com.juriki.api_juriki.usuario.dto.UsuarioCriarRequestDTO;
import com.juriki.api_juriki.usuario.dto.UsuarioResponseDTO;
import com.juriki.api_juriki.usuario.enums.EStatusConta;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PlanoRepository planoRepository;
    private final PasswordEncoder passwordEncoder;

    // -------------------------------------------------------
    // CRIAR
    // -------------------------------------------------------

    @Transactional
    public UsuarioResponseDTO criar(UsuarioCriarRequestDTO request) {

        // Valida e-mail duplicado
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflitoException("Já existe uma conta cadastrada com este e-mail.");
        }

        // Valida CPF duplicado (se informado)
        if (request.getCpf() != null && usuarioRepository.existsByCpf(request.getCpf())) {
            throw new ConflitoException("Já existe uma conta cadastrada com este CPF.");
        }

        // Busca plano (se informado)
        Plano plano = null;
        if (request.getIdPlano() != null) {
            plano = planoRepository.findById(request.getIdPlano())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado."));
        }

        // Monta entidade
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .dataNascimento(request.getDataNascimento())
                .tipoUsuario(
                    request.getTipoUsuario() != null
                        ? request.getTipoUsuario()
                        : ETipoUsuario.CLIENTE
                )
                .statusConta(EStatusConta.ATIVA)
                .plano(plano)
                .build();

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    // -------------------------------------------------------
    // BUSCAR POR ID (somente ativos)
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Integer idUsuario) {
        Usuario usuario = buscarUsuarioAtivo(idUsuario);
        return toResponse(usuario);
    }

    // -------------------------------------------------------
    // BUSCAR POR ID (administrativo — todos os status)
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorIdAdministrativo(Integer idUsuario) {
        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
        return toResponse(usuario);
    }

    // -------------------------------------------------------
    // LISTAR TODOS (somente ativos)
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository
                .findAllByStatusContaAndDeletadoEmIsNull(EStatusConta.ATIVA)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // LISTAR TODOS (administrativo — todos os status)
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodosAdministrativo() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // ATUALIZAR
    // -------------------------------------------------------

    @Transactional
    public UsuarioResponseDTO atualizar(Integer idUsuario, UsuarioAtualizarRequestDTO request) {

        Usuario usuario = buscarUsuarioAtivo(idUsuario);

        if (request.getNome() != null) {
            usuario.setNome(request.getNome());
        }
        if (request.getTelefone() != null) {
            usuario.setTelefone(request.getTelefone());
        }
        if (request.getDataNascimento() != null) {
            usuario.setDataNascimento(request.getDataNascimento());
        }
        if (request.getFotoPerfil() != null) {
            usuario.setFotoPerfil(request.getFotoPerfil());
        }

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    // -------------------------------------------------------
    // SOFT DELETE
    // -------------------------------------------------------

    @Transactional
    public void deletar(Integer idUsuario) {
        Usuario usuario = buscarUsuarioAtivo(idUsuario);
        usuario.setDeletadoEm(LocalDateTime.now());
        usuario.setStatusConta(EStatusConta.INATIVA);
        usuarioRepository.save(usuario);
    }

    // -------------------------------------------------------
    // HELPERS PRIVADOS
    // -------------------------------------------------------

    private Usuario buscarUsuarioAtivo(Integer idUsuario) {
        return usuarioRepository
                .findByIdAndStatusContaAndDeletadoEmIsNull(idUsuario, EStatusConta.ATIVA)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .telefone(usuario.getTelefone())
                .fotoPerfil(usuario.getFotoPerfil())
                .dataNascimento(usuario.getDataNascimento())
                .tipoUsuario(usuario.getTipoUsuario())
                .statusConta(usuario.getStatusConta())
                .emailVerificado(usuario.getEmailVerificado())
                .ultimoLogin(usuario.getUltimoLogin())
                .criadoEm(usuario.getCriadoEm())
                .atualizadoEm(usuario.getAtualizadoEm())
                .idPlano(usuario.getPlano() != null ? usuario.getPlano().getId() : null)
                .nomePlano(usuario.getPlano() != null ? usuario.getPlano().getNomePlano() : null)
                .build();
    }
}