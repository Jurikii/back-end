package com.juriki.api_juriki.pagamento.controller;

import com.juriki.api_juriki.pagamento.dto.PagamentoAtualizarRequestDTO;
import com.juriki.api_juriki.pagamento.dto.PagamentoCriarRequestDTO;
import com.juriki.api_juriki.pagamento.dto.PagamentoResponseDTO;
import com.juriki.api_juriki.pagamento.service.PagamentoService;
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
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> criar(@RequestBody @Valid PagamentoCriarRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PagamentoResponseDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(pagamentoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/plano/{idPlano}")
    public ResponseEntity<List<PagamentoResponseDTO>> listarPorPlano(@PathVariable Integer idPlano) {
        return ResponseEntity.ok(pagamentoService.listarPorPlano(idPlano));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PagamentoAtualizarRequestDTO request) {
        return ResponseEntity.ok(pagamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
