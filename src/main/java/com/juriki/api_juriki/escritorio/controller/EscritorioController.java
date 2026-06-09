package com.juriki.api_juriki.escritorio.controller;

import com.juriki.api_juriki.escritorio.dto.EscritorioAtualizarRequestDTO;
import com.juriki.api_juriki.escritorio.dto.EscritorioCriarRequestDTO;
import com.juriki.api_juriki.escritorio.dto.EscritorioResponseDTO;
import com.juriki.api_juriki.escritorio.service.EscritorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/escritorios")
@RequiredArgsConstructor
public class EscritorioController {

    private final EscritorioService escritorioService;

    @PostMapping
    public ResponseEntity<EscritorioResponseDTO> criar(@RequestBody @Valid EscritorioCriarRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(escritorioService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscritorioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(escritorioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EscritorioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(escritorioService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscritorioResponseDTO> atualizar(@PathVariable Integer id, @RequestBody @Valid EscritorioAtualizarRequestDTO request) {
        return ResponseEntity.ok(escritorioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        escritorioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
