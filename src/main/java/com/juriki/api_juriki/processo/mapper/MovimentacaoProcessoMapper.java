package com.juriki.api_juriki.processo.mapper;

import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.MovimentacaoProcessoResponseDTO;
import com.juriki.api_juriki.processo.model.MovimentacaoProcesso;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoProcessoMapper {

    public MovimentacaoProcessoResponseDTO toResponseDTO(MovimentacaoProcesso movimentacao) {
        return MovimentacaoProcessoResponseDTO.builder()
                .id(movimentacao.getId())
                .idProcesso(movimentacao.getProcesso().getId())
                .tituloMovimentacao(movimentacao.getTituloMovimentacao())
                .descricao(movimentacao.getDescricao())
                .dataMovimentacao(movimentacao.getDataMovimentacao())
                .build();
    }

    public MovimentacaoProcesso toEntity(MovimentacaoProcessoCriarRequestDTO dto) {
        return MovimentacaoProcesso.builder()
                .tituloMovimentacao(dto.getTituloMovimentacao())
                .descricao(dto.getDescricao())
                .build();
    }
}
