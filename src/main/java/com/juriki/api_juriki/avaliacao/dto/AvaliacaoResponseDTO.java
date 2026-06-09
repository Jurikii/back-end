package com.juriki.api_juriki.avaliacao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AvaliacaoResponseDTO {

    private Integer idAvaliacao;
    private Integer idUsuario;
    private String nomeUsuario;
    private Integer idAdvogado;
    private String nomeAdvogado;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
}
