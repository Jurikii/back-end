package com.juriki.api_juriki.avaliacao.service;

import com.juriki.api_juriki.advogado.model.Advogado;
import com.juriki.api_juriki.advogado.repository.AdvogadoRepository;
import com.juriki.api_juriki.avaliacao.dto.AvaliacaoAtualizarRequestDTO;
import com.juriki.api_juriki.avaliacao.dto.AvaliacaoCriarRequestDTO;
import com.juriki.api_juriki.avaliacao.dto.AvaliacaoResponseDTO;
import com.juriki.api_juriki.avaliacao.model.Avaliacao;
import com.juriki.api_juriki.avaliacao.repository.AvaliacaoRepository;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdvogadoRepository advogadoRepository;

    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoCriarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        Advogado advogado = advogadoRepository.findById(request.getIdAdvogado())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Advogado não encontrado."));

        Avaliacao avaliacao = Avaliacao.builder()
                .usuario(usuario)
                .advogado(advogado)
                .nota(request.getNota())
                .comentario(request.getComentario())
                .build();

        avaliacaoRepository.save(avaliacao);

        recalcularNotaAdvogado(advogado);

        log.info("Avaliação criada: ID {} | Advogado ID {} | Nota {}", avaliacao.getId(), advogado.getId(), request.getNota());

        return toResponse(avaliacao);
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorId(Integer idAvaliacao) {
        return toResponse(buscarAvaliacao(idAvaliacao));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarTodas() {
        return avaliacaoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorAdvogado(Integer idAdvogado) {
        return avaliacaoRepository.findByAdvogadoId(idAdvogado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return avaliacaoRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AvaliacaoResponseDTO atualizar(Integer idAvaliacao, AvaliacaoAtualizarRequestDTO request) {
        Avaliacao avaliacao = buscarAvaliacao(idAvaliacao);

        if (request.getNota() != null) {
            avaliacao.setNota(request.getNota());
        }
        if (request.getComentario() != null) {
            avaliacao.setComentario(request.getComentario());
        }

        avaliacaoRepository.save(avaliacao);

        recalcularNotaAdvogado(avaliacao.getAdvogado());

        log.info("Avaliação ID {} atualizada.", idAvaliacao);

        return toResponse(avaliacao);
    }

    @Transactional
    public void deletar(Integer idAvaliacao) {
        Avaliacao avaliacao = buscarAvaliacao(idAvaliacao);
        Advogado advogado = avaliacao.getAdvogado();
        avaliacaoRepository.delete(avaliacao);

        recalcularNotaAdvogado(advogado);

        log.info("Avaliação ID {} removida.", idAvaliacao);
    }

    private void recalcularNotaAdvogado(Advogado advogado) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAdvogadoId(advogado.getId());

        if (avaliacoes.isEmpty()) {
            advogado.setNotaMedia(BigDecimal.ZERO);
            advogado.setTotalAvaliacoes(0);
        } else {
            double media = avaliacoes.stream()
                    .mapToInt(Avaliacao::getNota)
                    .average()
                    .orElse(0.0);

            advogado.setNotaMedia(BigDecimal.valueOf(media).setScale(2, RoundingMode.HALF_UP));
            advogado.setTotalAvaliacoes(avaliacoes.size());
        }

        advogadoRepository.save(advogado);
    }

    private Avaliacao buscarAvaliacao(Integer idAvaliacao) {
        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada."));
    }

    private AvaliacaoResponseDTO toResponse(Avaliacao avaliacao) {
        return AvaliacaoResponseDTO.builder()
                .idAvaliacao(avaliacao.getId())
                .idUsuario(avaliacao.getUsuario().getId())
                .nomeUsuario(avaliacao.getUsuario().getNome())
                .idAdvogado(avaliacao.getAdvogado().getId())
                .nomeAdvogado(avaliacao.getAdvogado().getUsuario().getNome())
                .nota(avaliacao.getNota())
                .comentario(avaliacao.getComentario())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .build();
    }
}
