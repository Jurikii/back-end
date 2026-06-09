package com.juriki.api_juriki.processo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProcessoAdvogadoResponseDTO {

    private Integer idVinculo;
    private Integer idProcesso;
    private Integer idAdvogado;
    private String nomeAdvogado;
    private String oabAdvogado;
    private String funcao;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataDesvinculo;
    private boolean ativo;
}
