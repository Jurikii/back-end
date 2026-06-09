package com.juriki.api_juriki.perfil_ia.dto;

import com.juriki.api_juriki.perfil_ia.enums.EEstiloResposta;
import com.juriki.api_juriki.perfil_ia.enums.ENivelJuridico;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PerfilIaUsuarioResponseDTO {

    private Integer idPerfil;
    private Integer idUsuario;
    private ENivelJuridico nivelJuridico;
    private EEstiloResposta estiloResposta;
    private String objetivoUso;
    private String areasInteresse;
    private String linguagemPreferida;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
