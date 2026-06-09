package com.juriki.api_juriki.chatbot.dto;

import java.time.LocalDateTime;

public record ChatRespostaDTO(
        Integer idMensagem,
        String pergunta,
        String resposta,
        LocalDateTime dataInteracao
) {}
