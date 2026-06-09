package com.juriki.api_juriki.processo.controller;

import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoAtualizarRequestDTO;
import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.ProcessoAdvogadoResponseDTO;
import com.juriki.api_juriki.processo.service.ProcessoAdvogadoService;
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
@RequestMapping("/api/processos-advogados")
@RequiredArgsConstructor
public class ProcessoAdvogadoController {

    private final ProcessoAdvogadoService processoAdvogadoService;

    @PostMapping
    public ResponseEntity<ProcessoAdvogadoResponseDTO> criar(@RequestBody @Valid ProcessoAdvogadoCriarRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoAdvogadoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoAdvogadoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(processoAdvogadoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProcessoAdvogadoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(processoAdvogadoService.listarTodas());
    }

    @GetMapping("/processo/{idProcesso}")
    public ResponseEntity<List<ProcessoAdvogadoResponseDTO>> listarPorProcesso(@PathVariable Integer idProcesso) {
        return ResponseEntity.ok(processoAdvogadoService.listarPorProcesso(idProcesso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessoAdvogadoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ProcessoAdvogadoAtualizarRequestDTO request) {
        return ResponseEntity.ok(processoAdvogadoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        processoAdvogadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
