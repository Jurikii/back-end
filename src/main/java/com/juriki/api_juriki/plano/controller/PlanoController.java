package com.juriki.api_juriki.plano.controller;

import com.juriki.api_juriki.plano.dto.PlanoAtualizarRequestDTO;
import com.juriki.api_juriki.plano.dto.PlanoCriarRequestDTO;
import com.juriki.api_juriki.plano.dto.PlanoResponseDTO;
import com.juriki.api_juriki.plano.service.PlanoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
@RequiredArgsConstructor
public class PlanoController {

    private final PlanoService planoService;

    // POST /api/planos
    @PostMapping
    public ResponseEntity<PlanoResponseDTO> criar(
            @RequestBody @Valid PlanoCriarRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(planoService.criar(request));
    }

    // GET /api/planos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(planoService.buscarPorId(id));
    }

    // GET /api/planos
    // Retorna todos — apenas admin usa essa rota
    @GetMapping
    public ResponseEntity<List<PlanoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(planoService.listarTodos());
    }

    // GET /api/planos/ativos
    // Retorna só os planos ativos — usada pelo frontend para exibir ao cliente
    @GetMapping("/ativos")
    public ResponseEntity<List<PlanoResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(planoService.listarAtivos());
    }

    // PUT /api/planos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PlanoAtualizarRequestDTO request) {

        return ResponseEntity.ok(planoService.atualizar(id, request));
    }

    // DELETE /api/planos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        planoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
