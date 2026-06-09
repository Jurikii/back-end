package com.juriki.api_juriki.processo.service;

import com.juriki.api_juriki.advogado.model.Advogado;
import com.juriki.api_juriki.advogado.repository.AdvogadoRepository;
import com.juriki.api_juriki.exception.ConflitoException;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoAtualizarRequestDTO;
import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoResponseDTO;
import com.juriki.api_juriki.processo.model.Processo;
import com.juriki.api_juriki.processo.model.ProcessoAdvogado;
import com.juriki.api_juriki.processo.repository.ProcessoAdvogadoRepository;
import com.juriki.api_juriki.processo.repository.ProcessoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessoAdvogadoService {

    private final ProcessoAdvogadoRepository processoAdvogadoRepository;
    private final ProcessoRepository processoRepository;
    private final AdvogadoRepository advogadoRepository;

    @Transactional
    public ProcessoAdvogadoResponseDTO criar(ProcessoAdvogadoCriarRequestDTO request) {
        log.info("Vinculando advogado ID {} ao processo ID {}", request.getIdAdvogado(), request.getIdProcesso());

        Processo processo = processoRepository.findById(request.getIdProcesso())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + request.getIdProcesso()));

        Advogado advogado = advogadoRepository.findById(request.getIdAdvogado())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Advogado não encontrado: " + request.getIdAdvogado()));

        if (processoAdvogadoRepository.findVinculoAtivo(request.getIdProcesso(), request.getIdAdvogado()).isPresent()) {
            throw new ConflitoException("Este advogado já está vinculado a este processo.");
        }

        ProcessoAdvogado vinculo = ProcessoAdvogado.builder()
                .processo(processo)
                .advogado(advogado)
                .funcao(request.getFuncao())
                .build();

        vinculo = processoAdvogadoRepository.save(vinculo);

        log.info("Vínculo ID {} criado — Advogado ID {} / Processo ID {}", vinculo.getId(), request.getIdAdvogado(), request.getIdProcesso());

        return toResponse(vinculo);
    }

    @Transactional(readOnly = true)
    public ProcessoAdvogadoResponseDTO buscarPorId(Integer idVinculo) {
        return toResponse(buscarVinculo(idVinculo));
    }

    @Transactional(readOnly = true)
    public List<ProcessoAdvogadoResponseDTO> listarTodas() {
        return processoAdvogadoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProcessoAdvogadoResponseDTO> listarPorProcesso(Integer idProcesso) {
        return processoAdvogadoRepository.findByProcessoIdAndAtivoTrue(idProcesso)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProcessoAdvogadoResponseDTO atualizar(Integer idVinculo, ProcessoAdvogadoAtualizarRequestDTO request) {
        ProcessoAdvogado vinculo = buscarVinculo(idVinculo);

        if (request.getFuncao() != null) {
            vinculo.setFuncao(request.getFuncao());
        }

        vinculo = processoAdvogadoRepository.save(vinculo);

        log.info("Vínculo ID {} atualizado.", idVinculo);

        return toResponse(vinculo);
    }

    @Transactional
    public void deletar(Integer idVinculo) {
        ProcessoAdvogado vinculo = buscarVinculo(idVinculo);
        vinculo.setAtivo(false);
        vinculo.setDataDesvinculo(LocalDateTime.now());
        processoAdvogadoRepository.save(vinculo);
        log.info("Vínculo ID {} desativado.", idVinculo);
    }

    private ProcessoAdvogado buscarVinculo(Integer idVinculo) {
        return processoAdvogadoRepository.findById(idVinculo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado: " + idVinculo));
    }

    private ProcessoAdvogadoResponseDTO toResponse(ProcessoAdvogado pa) {
        return ProcessoAdvogadoResponseDTO.builder()
                .idVinculo(pa.getId())
                .idProcesso(pa.getProcesso().getId())
                .idAdvogado(pa.getAdvogado().getId())
                .nomeAdvogado(pa.getAdvogado().getUsuario().getNome())
                .oabAdvogado(pa.getAdvogado().getOab())
                .funcao(pa.getFuncao())
                .dataEntrada(pa.getDataVinculo())
                .dataDesvinculo(pa.getDataDesvinculo())
                .ativo(pa.isAtivo())
                .build();
    }
}
