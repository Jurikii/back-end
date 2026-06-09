package com.juriki.api_juriki.sessaousuario.dto;

import java.time.LocalDateTime;

public record SessaoUsuarioResponseDTO(
        Integer id,
        Integer idUsuario,
        String token,
        String ip,
        String userAgent,
        LocalDateTime expiraEm,
        LocalDateTime criadoEm
) {}
