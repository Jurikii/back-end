package com.juriki.api_juriki.chatbot.mapper;

import com.juriki.api_juriki.chatbot.dto.ChatDetalheDTO;
import com.juriki.api_juriki.chatbot.dto.ChatMensagemDTO;
import com.juriki.api_juriki.chatbot.dto.ChatResumoDTO;
import com.juriki.api_juriki.chatbot.model.Chat;
import com.juriki.api_juriki.chatbot.model.ChatMensagem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public ChatResumoDTO toResumoDTO(Chat chat) {
        String ultimaPergunta = chat.getMensagens().isEmpty()
                ? ""
                : truncar(chat.getMensagens().get(chat.getMensagens().size() - 1).getPergunta(), 100);

        return new ChatResumoDTO(
                chat.getId(),
                chat.getTituloChat(),
                chat.getCriadoEm(),
                chat.getMensagens().size(),
                ultimaPergunta
        );
    }

    public ChatDetalheDTO toDetalheDTO(Chat chat) {
        List<ChatMensagemDTO> mensagensDTO = chat.getMensagens()
                .stream()
                .map(this::toMensagemDTO)
                .toList();

        return new ChatDetalheDTO(
                chat.getId(),
                chat.getTituloChat(),
                chat.getCriadoEm(),
                mensagensDTO
        );
    }

    public ChatMensagemDTO toMensagemDTO(ChatMensagem mensagem) {
        return new ChatMensagemDTO(
                mensagem.getId(),
                mensagem.getPergunta(),
                mensagem.getResposta(),
                mensagem.getDataInteracao()
        );
    }

    private String truncar(String texto, int limite) {
        if (texto == null) return "";
        return texto.length() <= limite ? texto : texto.substring(0, limite) + "...";
    }
}
