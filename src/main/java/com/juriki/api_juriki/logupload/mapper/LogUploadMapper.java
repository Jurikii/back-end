package com.juriki.api_juriki.logupload.mapper;

import com.juriki.api_juriki.logupload.dto.LogUploadResponseDTO;
import com.juriki.api_juriki.logupload.model.LogUpload;
import org.springframework.stereotype.Component;

@Component
public class LogUploadMapper {

    public LogUploadResponseDTO toResponseDTO(LogUpload log) {
        return new LogUploadResponseDTO(
                log.getId(),
                log.getUsuario().getId(),
                log.getNomeArquivo(),
                log.getBucket(),
                log.getStatusUpload(),
                log.getMensagem(),
                log.getDataUpload()
        );
    }
}
