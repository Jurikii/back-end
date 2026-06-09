package com.juriki.api_juriki.arquivo.controller;

import com.juriki.api_juriki.arquivo.dto.ArquivoResponseDTO;
import com.juriki.api_juriki.arquivo.dto.ArquivoUploadRequestDTO;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import com.juriki.api_juriki.arquivo.service.ArquivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ArquivoResponseDTO> upload(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("arquivo") MultipartFile file,
            @RequestPart("dados") @Valid ArquivoUploadRequestDTO dto
    ) {
        Integer idUsuario = extrairId(userDetails);
        ArquivoResponseDTO response = arquivoService.upload(idUsuario, file, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/meus")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<List<ArquivoResponseDTO>> listarMeus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) TipoArquivo tipo
    ) {
        Integer idUsuario = extrairId(userDetails);

        List<ArquivoResponseDTO> arquivos = tipo != null
                ? arquivoService.listarPorTipo(idUsuario, tipo)
                : arquivoService.listarPorUsuario(idUsuario);

        return ResponseEntity.ok(arquivos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ArquivoResponseDTO> buscarPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairId(userDetails);
        return ResponseEntity.ok(arquivoService.buscarPorId(id, idUsuario));
    }

    @GetMapping("/processo/{idProcesso}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<List<ArquivoResponseDTO>> listarPorProcesso(
            @PathVariable Integer idProcesso
    ) {
        return ResponseEntity.ok(arquivoService.listarPorProcesso(idProcesso));
    }

    @GetMapping("/chatbot/{idChatbot}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<List<ArquivoResponseDTO>> listarPorChatbot(
            @PathVariable Integer idChatbot
    ) {
        return ResponseEntity.ok(arquivoService.listarPorChatbot(idChatbot));
    }

    @GetMapping("/espaco")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<Map<String, Long>> buscarEspacoUsado(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairId(userDetails);
        long espacoUsado = arquivoService.buscarEspacoUsado(idUsuario);
        long limiteTotal = 500L * 1024 * 1024;
        return ResponseEntity.ok(Map.of(
                "usado", espacoUsado,
                "total", limiteTotal,
                "disponivel", limiteTotal - espacoUsado
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairId(userDetails);
        arquivoService.deletar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }

    private Integer extrairId(UserDetails userDetails) {
        return Integer.parseInt(userDetails.getUsername());
    }
}
