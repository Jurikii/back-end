package com.juriki.api_juriki.processo.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProcessoAtualizarRequestDTO {

    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    @Size(max = 50, message = "O número do processo deve ter no máximo 50 caracteres.")
    private String numeroProcesso;

    private String descricao;

    @Size(max = 150, message = "O tribunal deve ter no máximo 150 caracteres.")
    private String tribunal;

    @Size(max = 150, message = "A vara deve ter no máximo 150 caracteres.")
    private String vara;

    private LocalDate dataAbertura;

    private LocalDate dataEncerramento;
}
