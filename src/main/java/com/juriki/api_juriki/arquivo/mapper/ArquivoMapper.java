package com.juriki.api_juriki.arquivo.mapper;

import com.juriki.api_juriki.arquivo.dto.ArquivoResponseDTO;
import com.juriki.api_juriki.arquivo.model.Arquivo;
import org.springframework.stereotype.Component;

@Component
public class ArquivoMapper {

    public ArquivoResponseDTO toResponseDTO(Arquivo arquivo) {
        return new ArquivoResponseDTO(
                arquivo.getId(),
                arquivo.getUsuario().getId(),
                arquivo.getProcesso() != null ? arquivo.getProcesso().getId() : null,
                arquivo.getIdChatbot(),
                arquivo.getNomeArquivo(),
                arquivo.getNomeOriginal(),
                arquivo.getTipoArquivo(),
                arquivo.getExtensao(),
                arquivo.getTamanho(),
                arquivo.getBucket(),
                arquivo.getCaminho(),
                arquivo.getDescricao(),
                arquivo.getUrlPublica(),
                arquivo.getOrigem(),
                arquivo.getDataUpload()
        );
    }
}
