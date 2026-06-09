package com.juriki.api_juriki.plano.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanoAtualizarRequestDTO {

    @Size(max = 100, message = "O nome do plano deve ter no máximo 100 caracteres.")
    private String nomePlano;

    private String descricao;

    @DecimalMin(value = "0.0", inclusive = true, message = "O valor não pode ser negativo.")
    private BigDecimal valor;

    private String beneficios;

    @Min(value = 0, message = "O limite de chats não pode ser negativo.")
    private Integer limiteChatsMes;

    @Min(value = 0, message = "O limite de uploads não pode ser negativo.")
    private Integer limiteUploads;

    private Boolean acessoIaAvancada;

    private Boolean consultasIlimitadas;

    // Permite ativar ou desativar o plano sem deletar
    private Boolean ativo;
}
