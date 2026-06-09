package com.juriki.api_juriki.escritorio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @Builder
public class EscritorioResponseDTO {
    private Integer idEscritorio;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String logo;
    private String descricao;
    private LocalDateTime criadoEm;
}
