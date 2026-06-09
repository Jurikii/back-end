package com.juriki.api_juriki.chatbot.service;

import com.juriki.api_juriki.chatbot.dto.*;
import com.juriki.api_juriki.chatbot.exception.ChatNotFoundException;
import com.juriki.api_juriki.chatbot.mapper.ChatMapper;
import com.juriki.api_juriki.chatbot.model.Chat;
import com.juriki.api_juriki.chatbot.model.ChatMensagem;
import com.juriki.api_juriki.chatbot.repository.ChatMensagemRepository;
import com.juriki.api_juriki.chatbot.repository.ChatRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final ChatRepository chatRepository;
    private final ChatMensagemRepository chatMensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final IAService iaService;
    private final ChatMapper mapper;

    private static final int JANELA_CONTEXTO = 10;

    @Transactional
    public ChatDetalheDTO criarChat(Integer idUsuario, ChatCriarRequestDTO dto) {
        log.info("Criando novo chat para usuário ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        String titulo = (dto.tituloChat() != null && !dto.tituloChat().isBlank())
                ? dto.tituloChat()
                : "Conversa " + LocalDateTime.now().toLocalDate();

        Chat chat = Chat.builder()
                .usuario(usuario)
                .tituloChat(titulo)
                .build();

        chat = chatRepository.save(chat);
        log.info("Chat ID {} criado para usuário ID {}", chat.getId(), idUsuario);
        return mapper.toDetalheDTO(chat);
    }

    @Transactional
    public ChatRespostaDTO enviarPergunta(Integer idUsuario, Integer idChat, ChatPerguntaRequestDTO dto) {
        log.info("Usuário ID {} enviando pergunta no chat ID {}", idUsuario, idChat);

        Chat chat = buscarChatDoUsuario(idChat, idUsuario);

        List<ChatMensagem> historico = chatMensagemRepository
                .findUltimasMensagens(idChat, JANELA_CONTEXTO);

        IAService.IAResultado resultado = iaService.gerarResposta(
                dto.pergunta(),
                historico,
                null
        );

        ChatMensagem mensagem = ChatMensagem.builder()
                .chat(chat)
                .pergunta(dto.pergunta())
                .resposta(resultado.resposta())
                .build();
        mensagem = chatMensagemRepository.save(mensagem);

        log.info("Resposta da IA salva no chat ID {}", idChat);
        return new ChatRespostaDTO(
                mensagem.getId(),
                mensagem.getPergunta(),
                mensagem.getResposta(),
                mensagem.getDataInteracao()
        );
    }

    @Transactional(readOnly = true)
    public List<ChatResumoDTO> listarChats(Integer idUsuario) {
        return chatRepository
                .findByUsuarioIdOrderByCriadoEmDesc(idUsuario)
                .stream()
                .map(mapper::toResumoDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChatDetalheDTO buscarChat(Integer idUsuario, Integer idChat) {
        Chat chat = chatRepository.findByIdWithMensagens(idChat, idUsuario)
                .orElseThrow(() -> new ChatNotFoundException(idChat));
        return mapper.toDetalheDTO(chat);
    }

    @Transactional
    public ChatResumoDTO renomearChat(Integer idUsuario, Integer idChat, String novoTitulo) {
        Chat chat = buscarChatDoUsuario(idChat, idUsuario);
        chat.setTituloChat(novoTitulo);
        return mapper.toResumoDTO(chatRepository.save(chat));
    }

    @Transactional
    public void deletarChat(Integer idUsuario, Integer idChat) {
        Chat chat = buscarChatDoUsuario(idChat, idUsuario);
        chatRepository.delete(chat);
        log.info("Chat ID {} removido pelo usuário ID {}", idChat, idUsuario);
    }

    private Chat buscarChatDoUsuario(Integer idChat, Integer idUsuario) {
        return chatRepository.findByIdAndUsuarioId(idChat, idUsuario)
                .orElseThrow(() -> new ChatNotFoundException(idChat));
    }
}
