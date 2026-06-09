package com.juriki.api_juriki.processo.controller;

import com.juriki.api_juriki.processo.dto.*;
import com.juriki.api_juriki.processo.service.ProcessoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processos")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ProcessoResponseDTO> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProcessoCriarRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(processoService.criar(extrairId(userDetails), dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ProcessoResponseDTO>> listar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status
    ) {
        Integer idUsuario = extrairId(userDetails);
        List<ProcessoResponseDTO> lista = status != null
                ? processoService.listarPorStatus(idUsuario, status)
                : processoService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ProcessoResponseDTO>> listarMeus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status
    ) {
        return listar(userDetails, status);
    }

    @GetMapping("/advogado")
    @PreAuthorize("hasRole('ADVOGADO')")
    public ResponseEntity<List<ProcessoResponseDTO>> listarComoAdvogado(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(processoService.listarPorAdvogado(extrairId(userDetails)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ProcessoResponseDTO> buscarPorId(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(processoService.buscarPorId(extrairId(userDetails), id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ProcessoResponseDTO> atualizar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            @Valid @RequestBody ProcessoAtualizarRequestDTO dto
    ) {
        return ResponseEntity.ok(processoService.atualizar(extrairId(userDetails), id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ProcessoResponseDTO> atualizarStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            @Valid @RequestBody ProcessoDTO.StatusRequest dto
    ) {
        return ResponseEntity.ok(processoService.atualizarStatus(extrairId(userDetails), id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id
    ) {
        processoService.deletar(extrairId(userDetails), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/advogados")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ProcessoDTO.AdvogadoVinculado> vincularAdvogado(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            @Valid @RequestBody ProcessoDTO.VincularRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(processoService.vincularAdvogado(extrairId(userDetails), id, dto));
    }

    @DeleteMapping("/{id}/advogados/{idAdvogado}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> desvincularAdvogado(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer id,
            @PathVariable int idAdvogado
    ) {
        processoService.desvincularAdvogado(extrairId(userDetails), id, idAdvogado);
        return ResponseEntity.noContent().build();
    }

    private Integer extrairId(UserDetails userDetails) {
        return Integer.parseInt(userDetails.getUsername());
    }
}
