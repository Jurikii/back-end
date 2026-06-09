package com.juriki.api_juriki.avaliacao.controller;

import com.juriki.api_juriki.avaliacao.dto.AvaliacaoAtualizarRequestDTO;
import com.juriki.api_juriki.avaliacao.dto.AvaliacaoCriarRequestDTO;
import com.juriki.api_juriki.avaliacao.dto.AvaliacaoResponseDTO;
import com.juriki.api_juriki.avaliacao.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criar(@RequestBody @Valid AvaliacaoCriarRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(avaliacaoService.listarTodas());
    }

    @GetMapping("/advogado/{idAdvogado}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorAdvogado(@PathVariable Integer idAdvogado) {
        return ResponseEntity.ok(avaliacaoService.listarPorAdvogado(idAdvogado));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(avaliacaoService.listarPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AvaliacaoAtualizarRequestDTO request) {
        return ResponseEntity.ok(avaliacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
