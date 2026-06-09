package com.juriki.api_juriki.configuracao_ia.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConfiguracaoIaAtualizarRequestDTO {

    @Size(max = 100, message = "O modelo padrão deve ter no máximo 100 caracteres.")
    private String modeloPadrao;

    @DecimalMin(value = "0.0", message = "A temperatura deve ser no mínimo 0.0.")
    @DecimalMax(value = "1.0", message = "A temperatura deve ser no máximo 1.0.")
    private BigDecimal temperatura;

    private Boolean respostasLongas;

    private Boolean linguagemJuridica;

    private Boolean simplificarTermos;
}
