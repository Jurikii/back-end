package com.juriki.api_juriki.processo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProcessoResponseDTO {

    private Integer id;
    private Integer idUsuario;
    private String titulo;
    private String numeroProcesso;
    private String descricao;
    private String tribunal;
    private String vara;
    private String statusProcesso;
    private LocalDate dataAbertura;
    private LocalDate dataEncerramento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
