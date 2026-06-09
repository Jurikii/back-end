package com.juriki.api_juriki.processo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MovimentacaoProcessoResponseDTO {

    private Integer id;
    private Integer idProcesso;
    private String tituloMovimentacao;
    private String descricao;
    private LocalDateTime dataMovimentacao;
}
