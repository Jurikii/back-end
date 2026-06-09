package com.juriki.api_juriki.processo.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoAtualizarRequestDTO;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoResponseDTO;
import com.juriki.api_juriki.processo.mapper.MovimentacaoProcessoMapper;
import com.juriki.api_juriki.processo.model.MovimentacaoProcesso;
import com.juriki.api_juriki.processo.model.Processo;
import com.juriki.api_juriki.processo.repository.MovimentacaoProcessoRepository;
import com.juriki.api_juriki.processo.repository.ProcessoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimentacaoProcessoService {

    private final MovimentacaoProcessoRepository movimentacaoRepository;
    private final ProcessoRepository processoRepository;
    private final MovimentacaoProcessoMapper movimentacaoMapper;

    @Transactional
    public MovimentacaoProcessoResponseDTO criar(Integer idUsuario, Integer idProcesso, MovimentacaoProcessoCriarRequestDTO dto) {
        Processo processo = processoRepository.findByIdAndUsuarioId(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));

        MovimentacaoProcesso movimentacao = movimentacaoMapper.toEntity(dto);
        movimentacao.setProcesso(processo);

        movimentacao = movimentacaoRepository.save(movimentacao);
        log.info("Movimentação ID {} criada para processo ID {}", movimentacao.getId(), idProcesso);
        return movimentacaoMapper.toResponseDTO(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoProcessoResponseDTO> listarPorProcesso(Integer idUsuario, Integer idProcesso) {
        if (!processoRepository.existsById(idProcesso)) {
            throw new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso);
        }

        return movimentacaoRepository
                .findByProcessoIdOrderByDataMovimentacaoDesc(idProcesso)
                .stream()
                .map(movimentacaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoProcessoResponseDTO buscarPorId(Integer idUsuario, Integer idProcesso, Integer idMovimentacao) {
        Processo processo = processoRepository.findByIdAndUsuarioId(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));

        MovimentacaoProcesso movimentacao = movimentacaoRepository.findById(idMovimentacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao));

        if (!movimentacao.getProcesso().getId().equals(processo.getId())) {
            throw new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao);
        }

        return movimentacaoMapper.toResponseDTO(movimentacao);
    }

    @Transactional
    public MovimentacaoProcessoResponseDTO atualizar(Integer idUsuario, Integer idProcesso, Integer idMovimentacao, MovimentacaoProcessoAtualizarRequestDTO dto) {
        Processo processo = processoRepository.findByIdAndUsuarioId(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));

        MovimentacaoProcesso movimentacao = movimentacaoRepository.findById(idMovimentacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao));

        if (!movimentacao.getProcesso().getId().equals(processo.getId())) {
            throw new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao);
        }

        if (dto.getTituloMovimentacao() != null) {
            movimentacao.setTituloMovimentacao(dto.getTituloMovimentacao());
        }
        if (dto.getDescricao() != null) {
            movimentacao.setDescricao(dto.getDescricao());
        }

        movimentacao = movimentacaoRepository.save(movimentacao);
        log.info("Movimentação ID {} atualizada.", idMovimentacao);
        return movimentacaoMapper.toResponseDTO(movimentacao);
    }

    @Transactional
    public void deletar(Integer idUsuario, Integer idProcesso, Integer idMovimentacao) {
        Processo processo = processoRepository.findByIdAndUsuarioId(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));

        MovimentacaoProcesso movimentacao = movimentacaoRepository.findById(idMovimentacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao));

        if (!movimentacao.getProcesso().getId().equals(processo.getId())) {
            throw new RecursoNaoEncontradoException("Movimentação não encontrada: " + idMovimentacao);
        }

        movimentacaoRepository.delete(movimentacao);
        log.info("Movimentação ID {} removida.", idMovimentacao);
    }
}
