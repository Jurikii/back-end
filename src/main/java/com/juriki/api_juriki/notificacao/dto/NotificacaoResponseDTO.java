package com.juriki.api_juriki.notificacao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificacaoResponseDTO {

    private Integer idNotificacao;
    private Integer idUsuario;
    private String nomeUsuario;
    private String titulo;
    private String mensagem;
    private Boolean lida;
    private LocalDateTime dataEnvio;
}
