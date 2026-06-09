package com.juriki.api_juriki.atendimento.dto;

import com.juriki.api_juriki.atendimento.enums.StatusAtendimento;
import jakarta.validation.constraints.NotNull;

public record AtendimentoStatusDTO(

        @NotNull(message = "O status é obrigatório")
        StatusAtendimento status
) {}
