package com.juriki.api_juriki.logupload.controller;

import com.juriki.api_juriki.logupload.dto.LogUploadAtualizarRequestDTO;
import com.juriki.api_juriki.logupload.dto.LogUploadCriarRequestDTO;
import com.juriki.api_juriki.logupload.dto.LogUploadResponseDTO;
import com.juriki.api_juriki.logupload.service.LogUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs-uploads")
@RequiredArgsConstructor
public class LogUploadController {

    private final LogUploadService service;

    @PostMapping
    public ResponseEntity<LogUploadResponseDTO> criar(@Valid @RequestBody LogUploadCriarRequestDTO dto) {
        LogUploadResponseDTO response = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LogUploadResponseDTO>> listar(
            @RequestParam(required = false) Integer idUsuario
    ) {
        if (idUsuario != null) {
            return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogUploadResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogUploadResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody LogUploadAtualizarRequestDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
