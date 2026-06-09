package com.juriki.api_juriki.atendimento.exception;

import com.juriki.api_juriki.atendimento.enums.StatusAtendimento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StatusInvalidoException extends RuntimeException {
    public StatusInvalidoException(StatusAtendimento atual, StatusAtendimento novo) {
        super("Transição de status inválida: " + atual + " → " + novo);
    }
}
