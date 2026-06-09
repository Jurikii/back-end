package com.juriki.api_juriki.workspace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WorkspaceResponseDTO {

    private Integer idWorkspace;
    private String nome;
    private String descricao;
    private Integer idUsuarioDono;
    private LocalDateTime criadoEm;
}
