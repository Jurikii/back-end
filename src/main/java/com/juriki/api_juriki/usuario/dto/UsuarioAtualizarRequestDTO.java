package com.juriki.api_juriki.usuario.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioAtualizarRequestDTO {

    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String nome;

    @Pattern(
        regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$",
        message = "Telefone inválido. Use o formato (00) 00000-0000."
    )
    private String telefone;

    private LocalDate dataNascimento;

    @Size(max = 500, message = "URL da foto inválida.")
    private String fotoPerfil;
}
