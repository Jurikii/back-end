package com.juriki.api_juriki.auth.controller;

import com.juriki.api_juriki.auth.dto.AuthResponseDTO;
import com.juriki.api_juriki.auth.dto.LoginRequestDTO;
import com.juriki.api_juriki.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest
    ) {
        AuthResponseDTO response = authService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Token não informado ou formato inválido"));
        }
        String token = authorization.substring(7);
        authService.logout(token);
        return ResponseEntity.ok(Map.of("mensagem", "Logout realizado com sucesso"));
    }
}
