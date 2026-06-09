package com.juriki.api_juriki.perfil_ia.controller;

import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioAtualizarRequestDTO;
import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioCriarRequestDTO;
import com.juriki.api_juriki.perfil_ia.dto.PerfilIaUsuarioResponseDTO;
import com.juriki.api_juriki.perfil_ia.service.PerfilIaUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfil-ia")
@RequiredArgsConstructor
public class PerfilIaUsuarioController {

    private final PerfilIaUsuarioService perfilIaUsuarioService;

    @PostMapping
    public ResponseEntity<PerfilIaUsuarioResponseDTO> criar(
            @RequestBody @Valid PerfilIaUsuarioCriarRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(perfilIaUsuarioService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilIaUsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilIaUsuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PerfilIaUsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(perfilIaUsuarioService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PerfilIaUsuarioResponseDTO>> listarPorUsuario(
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(perfilIaUsuarioService.listarPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilIaUsuarioResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PerfilIaUsuarioAtualizarRequestDTO request) {

        return ResponseEntity.ok(perfilIaUsuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        perfilIaUsuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
