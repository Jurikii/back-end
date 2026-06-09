package com.juriki.api_juriki.chatbot.dto;

import jakarta.validation.constraints.Size;

public record ChatCriarRequestDTO(

        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String tituloChat
) {}
