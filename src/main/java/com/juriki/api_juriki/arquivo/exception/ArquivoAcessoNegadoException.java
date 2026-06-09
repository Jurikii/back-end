package com.juriki.api_juriki.arquivo.exception;

public class ArquivoAcessoNegadoException extends RuntimeException {
    public ArquivoAcessoNegadoException() {
        super("Você não tem permissão para acessar este arquivo.");
    }
}
