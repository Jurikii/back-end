package com.juriki.api_juriki.auth.dto;

import java.time.LocalDateTime;

public record OAuthLoginResponseDTO(
        String token,
        String tipo,
        Integer idUsuario,
        String nome,
        String email,
        String fotoPerfil,
        boolean novoUsuario,
        LocalDateTime expiraEm
) {}
