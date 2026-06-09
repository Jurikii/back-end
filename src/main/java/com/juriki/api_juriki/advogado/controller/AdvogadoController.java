package com.juriki.api_juriki.advogado.controller;
import com.juriki.api_juriki.advogado.dto.AdvogadoAtualizarRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoCadastroCompletoRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoCriarRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoResponseDTO;
import com.juriki.api_juriki.advogado.service.AdvogadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advogados")
@RequiredArgsConstructor
public class AdvogadoController {

    private final AdvogadoService advogadoService;

    // POST /api/advogados
    @PostMapping
    public ResponseEntity<AdvogadoResponseDTO> criar(
            @RequestBody @Valid AdvogadoCriarRequestDTO request) {

        AdvogadoResponseDTO response = advogadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /api/advogados/cadastro-completo
    @PostMapping("/cadastro-completo")
    public ResponseEntity<AdvogadoResponseDTO> cadastroCompleto(
            @RequestBody @Valid AdvogadoCadastroCompletoRequestDTO request) {

        AdvogadoResponseDTO response = advogadoService.cadastroCompleto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/advogados/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AdvogadoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(advogadoService.buscarPorId(id));
    }

    // GET /api/advogados
    @GetMapping
    public ResponseEntity<List<AdvogadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(advogadoService.listarTodos());
    }

    // GET /api/advogados/aprovados
    @GetMapping("/aprovados")
    public ResponseEntity<List<AdvogadoResponseDTO>> listarAprovados() {
        return ResponseEntity.ok(advogadoService.listarAprovados());
    }

    // GET /api/advogados/buscar?especialidade=tributario
    @GetMapping("/buscar")
    public ResponseEntity<List<AdvogadoResponseDTO>> buscarPorEspecialidade(
            @RequestParam String especialidade) {

        return ResponseEntity.ok(advogadoService.buscarPorEspecialidade(especialidade));
    }

    // PUT /api/advogados/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AdvogadoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AdvogadoAtualizarRequestDTO request) {

        return ResponseEntity.ok(advogadoService.atualizar(id, request));
    }

    // PATCH /api/advogados/{id}/aprovar
    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<AdvogadoResponseDTO> aprovar(@PathVariable Integer id) {
        return ResponseEntity.ok(advogadoService.aprovar(id));
    }

    // DELETE /api/advogados/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        advogadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
