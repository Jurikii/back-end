package com.juriki.api_juriki.notificacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacaoCriarRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres.")
    private String titulo;

    private String mensagem;
}
