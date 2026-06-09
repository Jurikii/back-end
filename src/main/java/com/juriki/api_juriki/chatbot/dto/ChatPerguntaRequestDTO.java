package com.juriki.api_juriki.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatPerguntaRequestDTO(

        @NotBlank(message = "A pergunta não pode estar vazia")
        @Size(max = 4000, message = "A pergunta deve ter no máximo 4000 caracteres")
        String pergunta
) {}
