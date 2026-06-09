package com.juriki.api_juriki.chatbot.dto;

import java.time.LocalDateTime;

public record ChatMensagemDTO(
        Integer id,
        String pergunta,
        String resposta,
        LocalDateTime dataInteracao
) {}
