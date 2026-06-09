package com.juriki.api_juriki.workspace.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceAtualizarRequestDTO {

    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
    private String nome;

    private String descricao;
}
