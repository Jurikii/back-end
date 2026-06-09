package com.juriki.api_juriki.advogado.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AdvogadoCadastroCompletoRequestDTO {

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

    @NotBlank(message = "O número da OAB é obrigatório.")
    @Size(max = 20, message = "O número da OAB deve ter no máximo 20 caracteres.")
    private String oab;

    @NotBlank(message = "O estado da OAB é obrigatório.")
    @Size(min = 2, max = 2, message = "Informe a UF com 2 letras. Exemplo: SP, RJ, MG.")
    private String estado;

    @Size(max = 150, message = "A especialidade deve ter no máximo 150 caracteres.")
    private String especialidade;

    private String biografia;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor da consulta deve ser maior que zero.")
    private BigDecimal valorConsulta;

    @Min(value = 0, message = "Os anos de experiência não podem ser negativos.")
    private Integer experienciaAnos;

    @Size(max = 255, message = "URL do LinkedIn inválida.")
    private String linkedin;

    @Size(max = 255, message = "URL do site profissional inválida.")
    private String siteProfissional;
}
