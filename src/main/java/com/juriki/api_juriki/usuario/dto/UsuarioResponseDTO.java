package com.juriki.api_juriki.usuario.dto;

import com.juriki.api_juriki.usuario.enums.EStatusConta;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String fotoPerfil;
    private LocalDate dataNascimento;
    private ETipoUsuario tipoUsuario;
    private EStatusConta statusConta;
    private Boolean emailVerificado;
    private LocalDateTime ultimoLogin;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // Dados do plano resumidos
    private Integer idPlano;
    private String nomePlano;
}