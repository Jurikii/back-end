package com.juriki.api_juriki.pagamento.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PagamentoResponseDTO {

    private Integer idPagamento;
    private Integer idUsuario;
    private String nomeUsuario;
    private Integer idPlano;
    private String nomePlano;
    private BigDecimal valor;
    private String metodoPagamento;
    private String statusPagamento;
    private LocalDateTime dataPagamento;
}
