package com.juriki.api_juriki.logupload.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.logupload.dto.LogUploadAtualizarRequestDTO;
import com.juriki.api_juriki.logupload.dto.LogUploadCriarRequestDTO;
import com.juriki.api_juriki.logupload.dto.LogUploadResponseDTO;
import com.juriki.api_juriki.logupload.mapper.LogUploadMapper;
import com.juriki.api_juriki.logupload.model.LogUpload;
import com.juriki.api_juriki.logupload.repository.LogUploadRepository;
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
public class LogUploadService {

    private final LogUploadRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final LogUploadMapper mapper;

    @Transactional
    public LogUploadResponseDTO criar(LogUploadCriarRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + dto.idUsuario()));

        LogUpload logEntry = LogUpload.builder()
                .usuario(usuario)
                .nomeArquivo(dto.nomeArquivo())
                .bucket(dto.bucket())
                .statusUpload(dto.statusUpload())
                .mensagem(dto.mensagem())
                .build();

        logEntry = repository.save(logEntry);
        log.info("Log de upload criado: ID {}", logEntry.getId());
        return mapper.toResponseDTO(logEntry);
    }

    @Transactional(readOnly = true)
    public List<LogUploadResponseDTO> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LogUploadResponseDTO> listarPorUsuario(Integer idUsuario) {
        return repository.findByUsuarioIdOrderByDataUploadDesc(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public LogUploadResponseDTO buscarPorId(Integer id) {
        LogUpload logEntry = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Log de upload não encontrado: " + id));
        return mapper.toResponseDTO(logEntry);
    }

    @Transactional
    public LogUploadResponseDTO atualizar(Integer id, LogUploadAtualizarRequestDTO dto) {
        LogUpload logEntry = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Log de upload não encontrado: " + id));

        if (dto.nomeArquivo() != null) logEntry.setNomeArquivo(dto.nomeArquivo());
        if (dto.bucket() != null) logEntry.setBucket(dto.bucket());
        if (dto.statusUpload() != null) logEntry.setStatusUpload(dto.statusUpload());
        if (dto.mensagem() != null) logEntry.setMensagem(dto.mensagem());

        logEntry = repository.save(logEntry);
        log.info("Log de upload atualizado: ID {}", logEntry.getId());
        return mapper.toResponseDTO(logEntry);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Log de upload não encontrado: " + id);
        }
        repository.deleteById(id);
        log.info("Log de upload deletado: ID {}", id);
    }
}
