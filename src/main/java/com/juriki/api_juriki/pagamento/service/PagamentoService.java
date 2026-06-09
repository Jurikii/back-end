package com.juriki.api_juriki.pagamento.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.pagamento.dto.PagamentoAtualizarRequestDTO;
import com.juriki.api_juriki.pagamento.dto.PagamentoCriarRequestDTO;
import com.juriki.api_juriki.pagamento.dto.PagamentoResponseDTO;
import com.juriki.api_juriki.pagamento.model.Pagamento;
import com.juriki.api_juriki.pagamento.repository.PagamentoRepository;
import com.juriki.api_juriki.plano.model.Plano;
import com.juriki.api_juriki.plano.repository.PlanoRepository;
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
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanoRepository planoRepository;

    @Transactional
    public PagamentoResponseDTO criar(PagamentoCriarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        Plano plano = planoRepository.findById(request.getIdPlano())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado."));

        Pagamento pagamento = Pagamento.builder()
                .usuario(usuario)
                .plano(plano)
                .valor(request.getValor())
                .metodoPagamento(request.getMetodoPagamento())
                .statusPagamento(request.getStatusPagamento())
                .build();

        pagamentoRepository.save(pagamento);

        log.info("Pagamento criado: ID {} | Usuário ID {} | Plano ID {} | Valor {}",
                pagamento.getId(), usuario.getId(), plano.getId(), pagamento.getValor());

        return toResponse(pagamento);
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO buscarPorId(Integer idPagamento) {
        return toResponse(buscarPagamento(idPagamento));
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return pagamentoRepository.findByUsuarioIdOrderByDataPagamentoDesc(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorPlano(Integer idPlano) {
        return pagamentoRepository.findByPlanoIdOrderByDataPagamentoDesc(idPlano)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PagamentoResponseDTO atualizar(Integer idPagamento, PagamentoAtualizarRequestDTO request) {
        Pagamento pagamento = buscarPagamento(idPagamento);

        if (request.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
            pagamento.setUsuario(usuario);
        }

        if (request.getIdPlano() != null) {
            Plano plano = planoRepository.findById(request.getIdPlano())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado."));
            pagamento.setPlano(plano);
        }

        if (request.getValor() != null) {
            pagamento.setValor(request.getValor());
        }

        if (request.getMetodoPagamento() != null) {
            pagamento.setMetodoPagamento(request.getMetodoPagamento());
        }

        if (request.getStatusPagamento() != null) {
            pagamento.setStatusPagamento(request.getStatusPagamento());
        }

        pagamentoRepository.save(pagamento);

        log.info("Pagamento ID {} atualizado.", idPagamento);

        return toResponse(pagamento);
    }

    @Transactional
    public void deletar(Integer idPagamento) {
        Pagamento pagamento = buscarPagamento(idPagamento);
        pagamentoRepository.delete(pagamento);

        log.info("Pagamento ID {} removido.", idPagamento);
    }

    private Pagamento buscarPagamento(Integer idPagamento) {
        return pagamentoRepository.findById(idPagamento)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado."));
    }

    private PagamentoResponseDTO toResponse(Pagamento pagamento) {
        return PagamentoResponseDTO.builder()
                .idPagamento(pagamento.getId())
                .idUsuario(pagamento.getUsuario().getId())
                .nomeUsuario(pagamento.getUsuario().getNome())
                .idPlano(pagamento.getPlano().getId())
                .nomePlano(pagamento.getPlano().getNomePlano())
                .valor(pagamento.getValor())
                .metodoPagamento(pagamento.getMetodoPagamento())
                .statusPagamento(pagamento.getStatusPagamento())
                .dataPagamento(pagamento.getDataPagamento())
                .build();
    }
}
