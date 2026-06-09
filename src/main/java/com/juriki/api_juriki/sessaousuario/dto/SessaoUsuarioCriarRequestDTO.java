package com.juriki.api_juriki.sessaousuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SessaoUsuarioCriarRequestDTO(

        @NotNull(message = "O usuário é obrigatório")
        Integer idUsuario,

        @NotBlank(message = "O token é obrigatório")
        @Size(max = 500, message = "O token deve ter no máximo 500 caracteres")
        String token,

        @Size(max = 100, message = "O IP deve ter no máximo 100 caracteres")
        String ip,

        String userAgent,

        LocalDateTime expiraEm
) {}
