package com.juriki.api_juriki.workspace.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceCriarRequestDTO {

    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
    private String nome;

    private String descricao;

    @NotNull(message = "O ID do usuário dono é obrigatório.")
    private Integer idUsuarioDono;
}
