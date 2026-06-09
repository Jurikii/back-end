package com.juriki.api_juriki.chatbot.dto;

import java.time.LocalDateTime;

public record ChatResumoDTO(
        Integer id,
        String tituloChat,
        LocalDateTime criadoEm,
        int totalMensagens,
        String ultimaPergunta
) {}
