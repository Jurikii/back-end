package com.juriki.api_juriki.processo.controller;

import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoAtualizarRequestDTO;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoResponseDTO;
import com.juriki.api_juriki.processo.service.MovimentacaoProcessoService;
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
@RequestMapping("/api/processos/{idProcesso}/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoProcessoController {

    private final MovimentacaoProcessoService movimentacaoService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MovimentacaoProcessoResponseDTO> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idProcesso,
            @Valid @RequestBody MovimentacaoProcessoCriarRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movimentacaoService.criar(extrairId(userDetails), idProcesso, dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<List<MovimentacaoProcessoResponseDTO>> listar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idProcesso
    ) {
        return ResponseEntity.ok(movimentacaoService.listarPorProcesso(extrairId(userDetails), idProcesso));
    }

    @GetMapping("/{idMovimentacao}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<MovimentacaoProcessoResponseDTO> buscarPorId(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idProcesso,
            @PathVariable Integer idMovimentacao
    ) {
        return ResponseEntity.ok(movimentacaoService.buscarPorId(extrairId(userDetails), idProcesso, idMovimentacao));
    }

    @PutMapping("/{idMovimentacao}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MovimentacaoProcessoResponseDTO> atualizar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idProcesso,
            @PathVariable Integer idMovimentacao,
            @Valid @RequestBody MovimentacaoProcessoAtualizarRequestDTO dto
    ) {
        return ResponseEntity.ok(movimentacaoService.atualizar(extrairId(userDetails), idProcesso, idMovimentacao, dto));
    }

    @DeleteMapping("/{idMovimentacao}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idProcesso,
            @PathVariable Integer idMovimentacao
    ) {
        movimentacaoService.deletar(extrairId(userDetails), idProcesso, idMovimentacao);
        return ResponseEntity.noContent().build();
    }

    private Integer extrairId(UserDetails userDetails) {
        return Integer.parseInt(userDetails.getUsername());
    }
}
