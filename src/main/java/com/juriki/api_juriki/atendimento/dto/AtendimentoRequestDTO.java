package com.juriki.api_juriki.atendimento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AtendimentoRequestDTO(

        @NotNull(message = "O ID do advogado é obrigatório")
        Integer idAdvogado,

        @NotNull(message = "A data da consulta é obrigatória")
        @Future(message = "A data da consulta deve ser uma data futura")
        LocalDateTime dataConsulta,

        @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
        String descricao,

        String observacoes,

        String linkReuniao
) {}
