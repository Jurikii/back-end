package com.juriki.api_juriki.workspace.controller;

import com.juriki.api_juriki.workspace.dto.WorkspaceAtualizarRequestDTO;
import com.juriki.api_juriki.workspace.dto.WorkspaceCriarRequestDTO;
import com.juriki.api_juriki.workspace.dto.WorkspaceResponseDTO;
import com.juriki.api_juriki.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceResponseDTO> criar(
            @RequestBody @Valid WorkspaceCriarRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(workspaceService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDTO>> listarTodos() {
        return ResponseEntity.ok(workspaceService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<WorkspaceResponseDTO>> listarPorUsuario(
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(workspaceService.listarPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid WorkspaceAtualizarRequestDTO request) {

        return ResponseEntity.ok(workspaceService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        workspaceService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
