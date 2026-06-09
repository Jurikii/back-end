package com.juriki.api_juriki.processo.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentacaoProcessoCriarRequestDTO {

    @Size(max = 255, message = "O título da movimentação deve ter no máximo 255 caracteres.")
    private String tituloMovimentacao;

    private String descricao;
}
