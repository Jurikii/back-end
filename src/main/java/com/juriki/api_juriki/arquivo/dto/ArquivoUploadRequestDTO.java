package com.juriki.api_juriki.arquivo.dto;

import com.juriki.api_juriki.arquivo.enums.OrigemArquivo;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import jakarta.validation.constraints.NotNull;

public record ArquivoUploadRequestDTO(

        @NotNull(message = "O tipo do arquivo é obrigatório")
        TipoArquivo tipoArquivo,

        @NotNull(message = "A origem do arquivo é obrigatória")
        OrigemArquivo origem,

        Integer idProcesso,

        Integer idChatbot
) {}
