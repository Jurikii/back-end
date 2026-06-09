package com.juriki.api_juriki.arquivo.exception;

public class TipoArquivoNaoPermitidoException extends RuntimeException {
    public TipoArquivoNaoPermitidoException(String extensao) {
        super("Tipo de arquivo não permitido: " + extensao + ". Envie PDF, DOCX, PNG ou JPG.");
    }
}
