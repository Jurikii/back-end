package com.juriki.api_juriki.advogado.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvogadoAtualizarRequestDTO {

    @Size(max = 150, message = "A especialidade deve ter no máximo 150 caracteres.")
    private String especialidade;

    private String biografia;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor da consulta deve ser maior que zero.")
    private BigDecimal valorConsulta;

    @Min(value = 0, message = "Os anos de experiência não podem ser negativos.")
    private Integer experienciaAnos;

    @Size(max = 255, message = "URL do LinkedIn inválida.")
    private String linkedin;

    @Size(max = 255, message = "URL do site profissional inválida.")
    private String siteProfissional;
}
