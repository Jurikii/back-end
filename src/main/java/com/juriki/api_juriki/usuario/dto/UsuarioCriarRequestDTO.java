package com.juriki.api_juriki.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.juriki.api_juriki.usuario.enums.ETipoUsuario;

@Getter
@Setter
public class UsuarioCriarRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;

    @Pattern(
        regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
        message = "CPF inválido. Use o formato 000.000.000-00."
    )
    private String cpf;

    @Pattern(
        regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$",
        message = "Telefone inválido. Use o formato (00) 00000-0000."
    )
    private String telefone;

    private LocalDate dataNascimento;

    private ETipoUsuario tipoUsuario;

    private Integer idPlano;
}