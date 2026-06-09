package com.juriki.api_juriki.logupload.dto;

import java.time.LocalDateTime;

public record LogUploadResponseDTO(
        Integer id,
        Integer idUsuario,
        String nomeArquivo,
        String bucket,
        String statusUpload,
        String mensagem,
        LocalDateTime dataUpload
) {}
