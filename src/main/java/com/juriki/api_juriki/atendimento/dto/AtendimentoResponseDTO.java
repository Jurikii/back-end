package com.juriki.api_juriki.atendimento.dto;

import com.juriki.api_juriki.atendimento.enums.StatusAtendimento;
import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
        Integer id,
        Integer idUsuario,
        String nomeUsuario,
        Integer idAdvogado,
        String nomeAdvogado,
        String oabAdvogado,
        String especialidadeAdvogado,
        String descricao,
        LocalDateTime dataConsulta,
        StatusAtendimento status,
        String observacoes,
        String linkReuniao,
        LocalDateTime criadoEm
) {}
