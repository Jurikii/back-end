package com.juriki.api_juriki.atendimento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AtendimentoNotFoundException extends RuntimeException {
    public AtendimentoNotFoundException(Integer id) {
        super("Atendimento não encontrado com ID: " + id);
    }
}
