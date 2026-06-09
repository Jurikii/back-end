package com.juriki.api_juriki.notificacao.controller;

import com.juriki.api_juriki.notificacao.dto.NotificacaoAtualizarRequestDTO;
import com.juriki.api_juriki.notificacao.dto.NotificacaoCriarRequestDTO;
import com.juriki.api_juriki.notificacao.dto.NotificacaoResponseDTO;
import com.juriki.api_juriki.notificacao.service.NotificacaoService;
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
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO> criar(@RequestBody @Valid NotificacaoCriarRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacaoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(notificacaoService.listarTodas());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/nao-lidas")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNaoLidas(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidasPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid NotificacaoAtualizarRequestDTO request) {
        return ResponseEntity.ok(notificacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        notificacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
