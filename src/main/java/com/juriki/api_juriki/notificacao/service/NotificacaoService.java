package com.juriki.api_juriki.notificacao.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.notificacao.dto.NotificacaoAtualizarRequestDTO;
import com.juriki.api_juriki.notificacao.dto.NotificacaoCriarRequestDTO;
import com.juriki.api_juriki.notificacao.dto.NotificacaoResponseDTO;
import com.juriki.api_juriki.notificacao.model.Notificacao;
import com.juriki.api_juriki.notificacao.repository.NotificacaoRepository;
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
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public NotificacaoResponseDTO criar(NotificacaoCriarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .titulo(request.getTitulo())
                .mensagem(request.getMensagem())
                .lida(false)
                .build();

        notificacaoRepository.save(notificacao);

        log.info("Notificação criada: ID {} | Usuário ID {}", notificacao.getId(), usuario.getId());

        return toResponse(notificacao);
    }

    @Transactional(readOnly = true)
    public NotificacaoResponseDTO buscarPorId(Integer idNotificacao) {
        return toResponse(buscarNotificacao(idNotificacao));
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> listarTodas() {
        return notificacaoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return notificacaoRepository.findByUsuarioIdOrderByDataEnvioDesc(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> listarNaoLidasPorUsuario(Integer idUsuario) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalseOrderByDataEnvioDesc(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public NotificacaoResponseDTO atualizar(Integer idNotificacao, NotificacaoAtualizarRequestDTO request) {
        Notificacao notificacao = buscarNotificacao(idNotificacao);

        if (request.getTitulo() != null) {
            notificacao.setTitulo(request.getTitulo());
        }
        if (request.getMensagem() != null) {
            notificacao.setMensagem(request.getMensagem());
        }
        if (request.getLida() != null) {
            notificacao.setLida(request.getLida());
        }

        notificacaoRepository.save(notificacao);

        log.info("Notificação ID {} atualizada.", idNotificacao);

        return toResponse(notificacao);
    }

    @Transactional
    public void deletar(Integer idNotificacao) {
        Notificacao notificacao = buscarNotificacao(idNotificacao);
        notificacaoRepository.delete(notificacao);

        log.info("Notificação ID {} removida.", idNotificacao);
    }

    private Notificacao buscarNotificacao(Integer idNotificacao) {
        return notificacaoRepository.findById(idNotificacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificação não encontrada."));
    }

    private NotificacaoResponseDTO toResponse(Notificacao notificacao) {
        return NotificacaoResponseDTO.builder()
                .idNotificacao(notificacao.getId())
                .idUsuario(notificacao.getUsuario().getId())
                .nomeUsuario(notificacao.getUsuario().getNome())
                .titulo(notificacao.getTitulo())
                .mensagem(notificacao.getMensagem())
                .lida(notificacao.getLida())
                .dataEnvio(notificacao.getDataEnvio())
                .build();
    }
}
