package com.juriki.api_juriki.perfil_ia.dto;

import com.juriki.api_juriki.perfil_ia.enums.EEstiloResposta;
import com.juriki.api_juriki.perfil_ia.enums.ENivelJuridico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilIaUsuarioCriarRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    private ENivelJuridico nivelJuridico;

    private EEstiloResposta estiloResposta;

    @Size(max = 255, message = "O objetivo de uso deve ter no máximo 255 caracteres.")
    private String objetivoUso;

    private String areasInteresse;

    @Size(max = 50, message = "A linguagem preferida deve ter no máximo 50 caracteres.")
    private String linguagemPreferida = "PT-BR";
}
