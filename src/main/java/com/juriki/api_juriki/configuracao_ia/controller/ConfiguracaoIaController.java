package com.juriki.api_juriki.configuracao_ia.controller;

import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaAtualizarRequestDTO;
import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaCriarRequestDTO;
import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaResponseDTO;
import com.juriki.api_juriki.configuracao_ia.service.ConfiguracaoIaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuracoes-ia")
@RequiredArgsConstructor
public class ConfiguracaoIaController {

    private final ConfiguracaoIaService configuracaoIaService;

    @PostMapping
    public ResponseEntity<ConfiguracaoIaResponseDTO> criar(
            @RequestBody @Valid ConfiguracaoIaCriarRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(configuracaoIaService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoIaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(configuracaoIaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConfiguracaoIaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(configuracaoIaService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ConfiguracaoIaResponseDTO> buscarPorUsuario(
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(configuracaoIaService.buscarPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoIaResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ConfiguracaoIaAtualizarRequestDTO request) {

        return ResponseEntity.ok(configuracaoIaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        configuracaoIaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
