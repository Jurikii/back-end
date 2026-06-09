package com.juriki.api_juriki.configuracao_ia.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConfiguracaoIaResponseDTO {

    private Integer idConfig;
    private Integer idUsuario;
    private String modeloPadrao;
    private BigDecimal temperatura;
    private Boolean respostasLongas;
    private Boolean linguagemJuridica;
    private Boolean simplificarTermos;
    private LocalDateTime criadoEm;
}
