package com.juriki.api_juriki.chatbot.repository;

import com.juriki.api_juriki.chatbot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findByUsuarioIdOrderByCriadoEmDesc(Integer idUsuario);

    Optional<Chat> findByIdAndUsuarioId(Integer idChat, Integer idUsuario);

    @Query("""
            SELECT c FROM Chat c
            LEFT JOIN FETCH c.mensagens
            WHERE c.id = :idChat
            AND c.usuario.id = :idUsuario
            """)
    Optional<Chat> findByIdWithMensagens(
            @Param("idChat") Integer idChat,
            @Param("idUsuario") Integer idUsuario
    );
}
