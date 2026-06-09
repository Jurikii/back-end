package com.juriki.api_juriki.chatbot.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatDetalheDTO(
        Integer id,
        String tituloChat,
        LocalDateTime criadoEm,
        List<ChatMensagemDTO> mensagens
) {}
