package com.juriki.api_juriki.plano.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PlanoResponseDTO {

    private Integer idPlano;
    private String nomePlano;
    private String descricao;
    private BigDecimal valor;
    private String beneficios;
    private Integer limiteChatsMes;
    private Integer limiteUploads;
    private Boolean acessoIaAvancada;
    private Boolean consultasIlimitadas;
    private Boolean ativo;
    private LocalDateTime criadoEm;
}
