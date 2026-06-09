package com.juriki.api_juriki.atendimento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflitoAgendaException extends RuntimeException {
    public ConflitoAgendaException() {
        super("O advogado já possui uma consulta agendada nesse horário.");
    }
}
