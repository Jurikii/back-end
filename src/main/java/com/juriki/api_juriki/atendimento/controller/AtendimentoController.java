package com.juriki.api_juriki.atendimento.controller;

import com.juriki.api_juriki.atendimento.dto.AtendimentoRequestDTO;
import com.juriki.api_juriki.atendimento.dto.AtendimentoResponseDTO;
import com.juriki.api_juriki.atendimento.dto.AtendimentoStatusDTO;
import com.juriki.api_juriki.atendimento.service.AtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.juriki.api_juriki.auth.security.UsuarioDetails;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    /**
     * POST /api/atendimentos
     * Cliente agenda um novo atendimento.
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<AtendimentoResponseDTO> agendar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AtendimentoRequestDTO dto
    ) {
        Integer idUsuario = extrairIdDoUsuario(userDetails);
        AtendimentoResponseDTO response = atendimentoService.agendar(idUsuario, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<AtendimentoResponseDTO>> listarMeusAtendimentos(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairIdDoUsuario(userDetails);
        return ResponseEntity.ok(atendimentoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/meus/proximo")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<AtendimentoResponseDTO> proximoAtendimento(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairIdDoUsuario(userDetails);
        AtendimentoResponseDTO proximo = atendimentoService.buscarProximoAtendimento(idUsuario);
        if (proximo == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(proximo);
    }

    @GetMapping("/advogado")
    @PreAuthorize("hasRole('ADVOGADO')")
    public ResponseEntity<List<AtendimentoResponseDTO>> listarAtendimentosAdvogado(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idAdvogado = extrairIdDoUsuario(userDetails);
        return ResponseEntity.ok(atendimentoService.listarPorAdvogado(idAdvogado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atendimentoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<AtendimentoResponseDTO> atualizarStatus(
            @PathVariable Integer id,
            @Valid @RequestBody AtendimentoStatusDTO dto
    ) {
        return ResponseEntity.ok(atendimentoService.atualizarStatus(id, dto));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<AtendimentoResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(atendimentoService.cancelar(id));
    }

    @PatchMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('ADVOGADO')")
    public ResponseEntity<AtendimentoResponseDTO> finalizar(@PathVariable Integer id) {
        return ResponseEntity.ok(atendimentoService.finalizar(id));
    }

    /**
     * GET /api/atendimentos/primeira-consulta
     * Verifica se é a primeira consulta do usuário com o advogado.
     * Usado pelo fluxo de integração WhatsApp BSP.
     */
    @GetMapping("/primeira-consulta")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Boolean> isPrimeiraConsulta(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer idAdvogado
    ) {
        Integer idUsuario = extrairIdDoUsuario(userDetails);
        return ResponseEntity.ok(atendimentoService.isPrimeiraConsulta(idUsuario, idAdvogado));
    }

    private Integer extrairIdDoUsuario(UserDetails userDetails) {
        if (userDetails == null) return 1;
        return ((UsuarioDetails) userDetails).getId();
    }
}
