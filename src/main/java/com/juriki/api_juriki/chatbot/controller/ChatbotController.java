package com.juriki.api_juriki.chatbot.controller;

import com.juriki.api_juriki.auth.security.UsuarioDetails;
import com.juriki.api_juriki.chatbot.dto.*;
import com.juriki.api_juriki.chatbot.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chats")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ChatDetalheDTO> criarChat(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChatCriarRequestDTO dto
    ) {
        Integer idUsuario = extrairId(userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chatbotService.criarChat(idUsuario, dto));
    }

    @GetMapping("/chats")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<List<ChatResumoDTO>> listarChats(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer idUsuario = extrairId(userDetails);
        return ResponseEntity.ok(chatbotService.listarChats(idUsuario));
    }

    @GetMapping("/chats/{idChat}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ChatDetalheDTO> buscarChat(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idChat
    ) {
        Integer idUsuario = extrairId(userDetails);
        return ResponseEntity.ok(chatbotService.buscarChat(idUsuario, idChat));
    }

    @PostMapping("/chats/{idChat}/mensagens")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ChatRespostaDTO> enviarPergunta(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idChat,
            @Valid @RequestBody ChatPerguntaRequestDTO dto
    ) {
        Integer idUsuario = extrairId(userDetails);
        return ResponseEntity.ok(chatbotService.enviarPergunta(idUsuario, idChat, dto));
    }

    @PatchMapping("/chats/{idChat}/titulo")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<ChatResumoDTO> renomearChat(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idChat,
            @RequestBody Map<String, String> body
    ) {
        Integer idUsuario = extrairId(userDetails);
        String novoTitulo = body.get("titulo");
        return ResponseEntity.ok(chatbotService.renomearChat(idUsuario, idChat, novoTitulo));
    }

    @DeleteMapping("/chats/{idChat}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADVOGADO')")
    public ResponseEntity<Void> deletarChat(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer idChat
    ) {
        Integer idUsuario = extrairId(userDetails);
        chatbotService.deletarChat(idUsuario, idChat);
        return ResponseEntity.noContent().build();
    }

    private Integer extrairId(UserDetails userDetails) {
        return ((UsuarioDetails) userDetails).getId();
    }
}
