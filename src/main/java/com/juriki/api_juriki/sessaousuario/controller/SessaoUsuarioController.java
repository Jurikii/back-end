package com.juriki.api_juriki.sessaousuario.controller;

import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioAtualizarRequestDTO;
import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioCriarRequestDTO;
import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioResponseDTO;
import com.juriki.api_juriki.sessaousuario.service.SessaoUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessoes-usuario")
@RequiredArgsConstructor
public class SessaoUsuarioController {

    private final SessaoUsuarioService service;

    @PostMapping
    public ResponseEntity<SessaoUsuarioResponseDTO> criar(@Valid @RequestBody SessaoUsuarioCriarRequestDTO dto) {
        SessaoUsuarioResponseDTO response = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SessaoUsuarioResponseDTO>> listar(
            @RequestParam(required = false) Integer idUsuario
    ) {
        if (idUsuario != null) {
            return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoUsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<SessaoUsuarioResponseDTO> buscarPorToken(@PathVariable String token) {
        return ResponseEntity.ok(service.buscarPorToken(token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessaoUsuarioResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SessaoUsuarioAtualizarRequestDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
