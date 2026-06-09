package com.juriki.api_juriki.processo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ProcessoDTO {

    // -----------------------------------------------------------------------
    // AdvogadoVinculado — dados do vínculo N:N dentro da resposta do processo
    // -----------------------------------------------------------------------
    public record AdvogadoVinculado(
            int idVinculo,
            int idAdvogado,
            String nomeAdvogado,
            String oabAdvogado,
            String funcao,
            LocalDateTime dataVinculo,
            boolean ativo
    ) {}

    // -----------------------------------------------------------------------
    // VincularRequest — para vincular um advogado ao processo
    // -----------------------------------------------------------------------
    public record VincularRequest(

            @NotNull(message = "O ID do advogado é obrigatório")
            int idAdvogado,

            @NotBlank(message = "A função do advogado é obrigatória")
            String funcao
    ) {}

    // -----------------------------------------------------------------------
    // StatusRequest — para atualizar apenas o status do processo
    // -----------------------------------------------------------------------
    public record StatusRequest(

            @NotBlank(message = "O status é obrigatório")
            String status
    ) {}
}
