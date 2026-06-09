package com.juriki.api_juriki.chatbot.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Integer id) {
        super("Chat não encontrado com ID: " + id);
    }
}
