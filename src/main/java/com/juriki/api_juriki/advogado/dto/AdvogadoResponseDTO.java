package com.juriki.api_juriki.advogado.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdvogadoResponseDTO {

    private Integer idAdvogado;

    // Dados do usuário vinculado
    private Integer idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
    private String fotoPerfilUsuario;

    // Dados profissionais
    private String oab;
    private String estado;
    private String especialidade;
    private String biografia;
    private BigDecimal valorConsulta;
    private Integer experienciaAnos;
    private String linkedin;
    private String siteProfissional;

    // Status
    private Boolean aprovado;
    private String statusProfissional;
    private BigDecimal notaMedia;
    private Integer totalAvaliacoes;

    private LocalDateTime criadoEm;
}
