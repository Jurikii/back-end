package com.juriki.api_juriki.escritorio.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EscritorioAtualizarRequestDTO {

    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
    private String nome;

    @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres.")
    private String cnpj;

    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres.")
    private String email;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String telefone;

    private String endereco;

    @Size(max = 500, message = "O logo deve ter no máximo 500 caracteres.")
    private String logo;

    private String descricao;
}
