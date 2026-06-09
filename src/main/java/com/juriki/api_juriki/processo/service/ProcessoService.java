package com.juriki.api_juriki.processo.service;

import com.juriki.api_juriki.advogado.model.Advogado;
import com.juriki.api_juriki.advogado.repository.AdvogadoRepository;
import com.juriki.api_juriki.exception.ConflitoException;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.processo.dto.*;
import com.juriki.api_juriki.processo.mapper.ProcessoMapper;
import com.juriki.api_juriki.processo.model.Processo;
import com.juriki.api_juriki.processo.model.ProcessoAdvogado;
import com.juriki.api_juriki.processo.repository.ProcessoAdvogadoRepository;
import com.juriki.api_juriki.processo.repository.ProcessoRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessoService {

    private final ProcessoRepository processoRepository;
    private final ProcessoAdvogadoRepository processoAdvogadoRepository;
    private final AdvogadoRepository advogadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcessoMapper processoMapper;

    @Transactional
    public ProcessoResponseDTO criar(Integer idUsuario, ProcessoCriarRequestDTO dto) {
        log.info("Criando processo para usuário ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + idUsuario));

        if (processoRepository.existsByNumeroProcesso(dto.getNumeroProcesso())) {
            throw new ConflitoException("Já existe um processo com o número: " + dto.getNumeroProcesso());
        }

        Processo processo = processoMapper.toEntity(dto);
        processo.setUsuario(usuario);

        processo = processoRepository.save(processo);
        log.info("Processo ID {} criado.", processo.getId());
        return processoMapper.toResponseDTO(processo);
    }

    @Transactional(readOnly = true)
    public List<ProcessoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return processoRepository
                .findByUsuarioIdOrderByDataAtualizacaoDesc(idUsuario)
                .stream()
                .map(processoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProcessoResponseDTO> listarPorStatus(Integer idUsuario, String statusProcesso) {
        return processoRepository
                .findByUsuarioIdAndStatusProcessoOrderByDataAtualizacaoDesc(idUsuario, statusProcesso)
                .stream()
                .map(processoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProcessoResponseDTO buscarPorId(Integer idUsuario, Integer idProcesso) {
        Processo processo = processoRepository
                .findByIdWithAdvogados(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));
        return processoMapper.toResponseDTO(processo);
    }

    @Transactional(readOnly = true)
    public List<ProcessoResponseDTO> listarPorAdvogado(int idAdvogado) {
        return processoRepository
                .findByAdvogadoAtivo(idAdvogado)
                .stream()
                .map(processoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ProcessoResponseDTO atualizar(Integer idUsuario, Integer idProcesso, ProcessoAtualizarRequestDTO dto) {
        Processo processo = buscarProcessoDoUsuario(idProcesso, idUsuario);

        if (dto.getNumeroProcesso() != null
                && !dto.getNumeroProcesso().equals(processo.getNumeroProcesso())
                && processoRepository.existsByNumeroProcesso(dto.getNumeroProcesso())) {
            throw new ConflitoException("Já existe um processo com o número: " + dto.getNumeroProcesso());
        }

        if (dto.getTitulo() != null) processo.setTitulo(dto.getTitulo());
        if (dto.getNumeroProcesso() != null) processo.setNumeroProcesso(dto.getNumeroProcesso());
        if (dto.getDescricao() != null) processo.setDescricao(dto.getDescricao());
        if (dto.getTribunal() != null) processo.setTribunal(dto.getTribunal());
        if (dto.getVara() != null) processo.setVara(dto.getVara());
        if (dto.getDataAbertura() != null) processo.setDataAbertura(dto.getDataAbertura());
        if (dto.getDataEncerramento() != null) processo.setDataEncerramento(dto.getDataEncerramento());

        return processoMapper.toResponseDTO(processoRepository.save(processo));
    }

    @Transactional
    public ProcessoResponseDTO atualizarStatus(Integer idUsuario, Integer idProcesso, ProcessoDTO.StatusRequest dto) {
        Processo processo = buscarProcessoDoUsuario(idProcesso, idUsuario);
        processo.setStatusProcesso(dto.status());
        log.info("Status do processo ID {} atualizado para {}", idProcesso, dto.status());
        return processoMapper.toResponseDTO(processoRepository.save(processo));
    }

    @Transactional
    public void deletar(Integer idUsuario, Integer idProcesso) {
        Processo processo = buscarProcessoDoUsuario(idProcesso, idUsuario);

        int advogadosAtivos = processoAdvogadoRepository.countByProcessoIdAndAtivoTrue(idProcesso);
        if (advogadosAtivos > 0) {
            throw new ConflitoException(
                    "Não é possível excluir um processo com advogados ativos. Desvincule-os primeiro."
            );
        }

        processoRepository.delete(processo);
        log.info("Processo ID {} removido.", idProcesso);
    }

    @Transactional
    public ProcessoDTO.AdvogadoVinculado vincularAdvogado(
            Integer idUsuario, Integer idProcesso, ProcessoDTO.VincularRequest dto
    ) {
        buscarProcessoDoUsuario(idProcesso, idUsuario);

        if (processoRepository.advogadoJaVinculado(idProcesso, dto.idAdvogado())) {
            throw new ConflitoException("Este advogado já está vinculado a este processo.");
        }

        Advogado advogado = advogadoRepository.findByIdAndUsuarioDeletadoEmIsNull(dto.idAdvogado())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Advogado não encontrado: " + dto.idAdvogado()));

        Processo processo = processoRepository.findById(idProcesso)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));

        ProcessoAdvogado vinculo = ProcessoAdvogado.builder()
                .processo(processo)
                .advogado(advogado)
                .funcao(dto.funcao())
                .build();

        vinculo = processoAdvogadoRepository.save(vinculo);
        log.info("Advogado ID {} vinculado ao processo ID {} como {}",
                dto.idAdvogado(), idProcesso, dto.funcao());
        return toAdvogadoVinculado(vinculo);
    }

    @Transactional
    public void desvincularAdvogado(Integer idUsuario, Integer idProcesso, int idAdvogado) {
        buscarProcessoDoUsuario(idProcesso, idUsuario);

        ProcessoAdvogado vinculo = processoAdvogadoRepository
                .findVinculoAtivo(idProcesso, idAdvogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo entre advogado e processo não encontrado ou já encerrado."
                ));

        vinculo.setAtivo(false);
        vinculo.setDataDesvinculo(LocalDateTime.now());
        processoAdvogadoRepository.save(vinculo);
        log.info("Advogado ID {} desvinculado do processo ID {}", idAdvogado, idProcesso);
    }

    private ProcessoDTO.AdvogadoVinculado toAdvogadoVinculado(ProcessoAdvogado pa) {
        return new ProcessoDTO.AdvogadoVinculado(
                pa.getId(),
                pa.getAdvogado().getId(),
                pa.getAdvogado().getUsuario().getNome(),
                pa.getAdvogado().getOab(),
                pa.getFuncao(),
                pa.getDataVinculo(),
                pa.isAtivo()
        );
    }

    private Processo buscarProcessoDoUsuario(Integer idProcesso, Integer idUsuario) {
        return processoRepository.findByIdAndUsuarioId(idProcesso, idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Processo não encontrado: " + idProcesso));
    }
}
