package com.juriki.api_juriki.chatbot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @Column(name = "pergunta", nullable = false, columnDefinition = "TEXT")
    private String pergunta;

    @Column(name = "resposta", nullable = false, columnDefinition = "TEXT")
    private String resposta;

    @Column(name = "data_interacao", updatable = false)
    private LocalDateTime dataInteracao;

    @PrePersist
    protected void onCreate() {
        dataInteracao = LocalDateTime.now();
    }
}
