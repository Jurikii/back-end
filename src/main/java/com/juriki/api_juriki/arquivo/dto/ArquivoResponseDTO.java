package com.juriki.api_juriki.arquivo.dto;

import com.juriki.api_juriki.arquivo.enums.OrigemArquivo;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import java.time.LocalDateTime;

public record ArquivoResponseDTO(
        Integer id,
        Integer idUsuario,
        Integer idProcesso,
        Integer idChatbot,
        String nomeArquivo,
        String nomeOriginal,
        TipoArquivo tipoArquivo,
        String extensao,
        Long tamanho,
        String bucket,
        String caminho,
        String descricao,
        String urlPublica,
        OrigemArquivo origem,
        LocalDateTime dataUpload
) {}
