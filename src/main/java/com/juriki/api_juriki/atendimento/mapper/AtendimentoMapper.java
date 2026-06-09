package com.juriki.api_juriki.atendimento.mapper;

import com.juriki.api_juriki.atendimento.dto.AtendimentoResponseDTO;
import com.juriki.api_juriki.atendimento.model.Atendimento;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoMapper {

    public AtendimentoResponseDTO toResponseDTO(Atendimento atendimento) {
        return new AtendimentoResponseDTO(
                atendimento.getId(),
                atendimento.getUsuario().getId(),
                atendimento.getUsuario().getNome(),
                atendimento.getAdvogado().getId(),
                atendimento.getAdvogado().getUsuario().getNome(),
                atendimento.getAdvogado().getOab(),
                atendimento.getAdvogado().getEspecialidade(),
                atendimento.getDescricao(),
                atendimento.getDataConsulta(),
                atendimento.getStatus(),
                atendimento.getObservacoes(),
                atendimento.getLinkReuniao(),
                atendimento.getCriadoEm()
        );
    }
}
