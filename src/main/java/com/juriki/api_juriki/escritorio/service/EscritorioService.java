package com.juriki.api_juriki.escritorio.service;

import com.juriki.api_juriki.escritorio.dto.EscritorioAtualizarRequestDTO;
import com.juriki.api_juriki.escritorio.dto.EscritorioCriarRequestDTO;
import com.juriki.api_juriki.escritorio.dto.EscritorioResponseDTO;
import com.juriki.api_juriki.escritorio.model.Escritorio;
import com.juriki.api_juriki.escritorio.repository.EscritorioRepository;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j @Service @RequiredArgsConstructor
public class EscritorioService {

    private final EscritorioRepository escritorioRepository;

    @Transactional
    public EscritorioResponseDTO criar(EscritorioCriarRequestDTO request) {
        Escritorio escritorio = Escritorio.builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .endereco(request.getEndereco())
                .logo(request.getLogo())
                .descricao(request.getDescricao())
                .build();
        escritorioRepository.save(escritorio);
        log.info("Escritório criado: ID {} | nome {}", escritorio.getId(), escritorio.getNome());
        return toResponse(escritorio);
    }

    @Transactional(readOnly = true)
    public EscritorioResponseDTO buscarPorId(Integer idEscritorio) {
        return toResponse(buscarEscritorio(idEscritorio));
    }

    @Transactional(readOnly = true)
    public List<EscritorioResponseDTO> listarTodos() {
        return escritorioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public EscritorioResponseDTO atualizar(Integer idEscritorio, EscritorioAtualizarRequestDTO request) {
        Escritorio escritorio = buscarEscritorio(idEscritorio);
        if (request.getNome() != null) escritorio.setNome(request.getNome());
        if (request.getCnpj() != null) escritorio.setCnpj(request.getCnpj());
        if (request.getEmail() != null) escritorio.setEmail(request.getEmail());
        if (request.getTelefone() != null) escritorio.setTelefone(request.getTelefone());
        if (request.getEndereco() != null) escritorio.setEndereco(request.getEndereco());
        if (request.getLogo() != null) escritorio.setLogo(request.getLogo());
        if (request.getDescricao() != null) escritorio.setDescricao(request.getDescricao());
        escritorioRepository.save(escritorio);
        log.info("Escritório ID {} atualizado.", idEscritorio);
        return toResponse(escritorio);
    }

    @Transactional
    public void deletar(Integer idEscritorio) {
        Escritorio escritorio = buscarEscritorio(idEscritorio);
        escritorioRepository.delete(escritorio);
        log.info("Escritório ID {} removido.", idEscritorio);
    }

    private Escritorio buscarEscritorio(Integer idEscritorio) {
        return escritorioRepository.findById(idEscritorio)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escritório não encontrado."));
    }

    private EscritorioResponseDTO toResponse(Escritorio escritorio) {
        return EscritorioResponseDTO.builder()
                .idEscritorio(escritorio.getId())
                .nome(escritorio.getNome())
                .cnpj(escritorio.getCnpj())
                .email(escritorio.getEmail())
                .telefone(escritorio.getTelefone())
                .endereco(escritorio.getEndereco())
                .logo(escritorio.getLogo())
                .descricao(escritorio.getDescricao())
                .criadoEm(escritorio.getCriadoEm())
                .build();
    }
}
