package com.juriki.api_juriki.chatbot.repository;

import com.juriki.api_juriki.chatbot.model.ChatMensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMensagemRepository extends JpaRepository<ChatMensagem, Integer> {

    @Query("""
            SELECT m FROM ChatMensagem m
            WHERE m.chat.id = :idChat
            ORDER BY m.dataInteracao DESC
            LIMIT :limite
            """)
    List<ChatMensagem> findUltimasMensagens(
            @Param("idChat") Integer idChat,
            @Param("limite") int limite
    );
}
