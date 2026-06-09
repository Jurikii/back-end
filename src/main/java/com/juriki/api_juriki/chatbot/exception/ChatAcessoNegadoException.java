package com.juriki.api_juriki.chatbot.exception;

public class ChatAcessoNegadoException extends RuntimeException {
    public ChatAcessoNegadoException() {
        super("Você não tem permissão para acessar este chat.");
    }
}
