package com.juriki.api_juriki.advogado.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdvogadoCriarRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @NotBlank(message = "O número da OAB é obrigatório.")
    @Size(max = 20, message = "O número da OAB deve ter no máximo 20 caracteres.")
    private String oab;

    @NotBlank(message = "O estado da OAB é obrigatório.")
    @Size(min = 2, max = 2, message = "Informe a UF com 2 letras. Exemplo: SP, RJ, MG.")
    private String estado;

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

