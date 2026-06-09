package com.juriki.api_juriki.processo.mapper;

import com.juriki.api_juriki.processo.dto.ProcessoCriarRequestDTO;
import com.juriki.api_juriki.processo.dto.ProcessoResponseDTO;
import com.juriki.api_juriki.processo.model.Processo;
import org.springframework.stereotype.Component;

@Component
public class ProcessoMapper {

    public ProcessoResponseDTO toResponseDTO(Processo processo) {
        return ProcessoResponseDTO.builder()
                .id(processo.getId())
                .idUsuario(processo.getUsuario().getId())
                .titulo(processo.getTitulo())
                .numeroProcesso(processo.getNumeroProcesso())
                .descricao(processo.getDescricao())
                .tribunal(processo.getTribunal())
                .vara(processo.getVara())
                .statusProcesso(processo.getStatusProcesso())
                .dataAbertura(processo.getDataAbertura())
                .dataEncerramento(processo.getDataEncerramento())
                .dataCriacao(processo.getDataCriacao())
                .dataAtualizacao(processo.getDataAtualizacao())
                .build();
    }

    public Processo toEntity(ProcessoCriarRequestDTO dto) {
        return Processo.builder()
                .titulo(dto.getTitulo())
                .numeroProcesso(dto.getNumeroProcesso())
                .descricao(dto.getDescricao())
                .tribunal(dto.getTribunal())
                .vara(dto.getVara())
                .dataAbertura(dto.getDataAbertura())
                .dataEncerramento(dto.getDataEncerramento())
                .build();
    }
}
