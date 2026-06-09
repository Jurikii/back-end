package com.juriki.api_juriki.avaliacao.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoCriarRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @NotNull(message = "O ID do advogado é obrigatório.")
    private Integer idAdvogado;

    @NotNull(message = "A nota é obrigatória.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    private Integer nota;

    private String comentario;
}
