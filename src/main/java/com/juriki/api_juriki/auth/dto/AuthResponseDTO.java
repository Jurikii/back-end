package com.juriki.api_juriki.auth.dto;

import java.time.LocalDateTime;

public record AuthResponseDTO(
        String token,
        String tipo,
        Integer idUsuario,
        String nome,
        String email,
        LocalDateTime expiraEm
) {}
