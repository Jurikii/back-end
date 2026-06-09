package com.juriki.api_juriki.plano.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanoCriarRequestDTO {

    @NotBlank(message = "O nome do plano é obrigatório.")
    @Size(max = 100, message = "O nome do plano deve ter no máximo 100 caracteres.")
    private String nomePlano;

    private String descricao;

    @NotNull(message = "O valor do plano é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O valor não pode ser negativo.")
    private BigDecimal valor;

    private String beneficios;

    @Min(value = 0, message = "O limite de chats não pode ser negativo.")
    private Integer limiteChatsMes = 0;

    @Min(value = 0, message = "O limite de uploads não pode ser negativo.")
    private Integer limiteUploads = 0;

    private Boolean acessoIaAvancada = false;

    private Boolean consultasIlimitadas = false;
}
