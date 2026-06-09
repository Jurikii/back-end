package com.juriki.api_juriki.logupload.dto;

import jakarta.validation.constraints.Size;

public record LogUploadAtualizarRequestDTO(

        @Size(max = 255, message = "O nome do arquivo deve ter no máximo 255 caracteres")
        String nomeArquivo,

        @Size(max = 100, message = "O bucket deve ter no máximo 100 caracteres")
        String bucket,

        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        String statusUpload,

        String mensagem
) {}
