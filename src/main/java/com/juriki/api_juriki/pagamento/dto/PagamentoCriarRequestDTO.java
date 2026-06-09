package com.juriki.api_juriki.pagamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagamentoCriarRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @NotNull(message = "O ID do plano é obrigatório.")
    private Integer idPlano;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @Size(max = 50, message = "O método de pagamento deve ter no máximo 50 caracteres.")
    private String metodoPagamento;

    @Size(max = 50, message = "O status do pagamento deve ter no máximo 50 caracteres.")
    private String statusPagamento;
}
