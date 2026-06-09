package com.juriki.api_juriki.processo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessoAdvogadoCriarRequestDTO {

    @NotNull(message = "O ID do processo é obrigatório")
    private Integer idProcesso;

    @NotNull(message = "O ID do advogado é obrigatório")
    private Integer idAdvogado;

    private String funcao;
}
