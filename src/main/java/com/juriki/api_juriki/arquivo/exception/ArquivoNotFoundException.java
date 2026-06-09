package com.juriki.api_juriki.arquivo.exception;

public class ArquivoNotFoundException extends RuntimeException {
    public ArquivoNotFoundException(Integer id) {
        super("Arquivo não encontrado com ID: " + id);
    }
}
