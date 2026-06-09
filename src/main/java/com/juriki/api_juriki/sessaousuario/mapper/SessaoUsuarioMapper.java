package com.juriki.api_juriki.sessaousuario.mapper;

import com.juriki.api_juriki.sessaousuario.dto.SessaoUsuarioResponseDTO;
import com.juriki.api_juriki.sessaousuario.model.SessaoUsuario;
import org.springframework.stereotype.Component;

@Component
public class SessaoUsuarioMapper {

    public SessaoUsuarioResponseDTO toResponseDTO(SessaoUsuario sessao) {
        return new SessaoUsuarioResponseDTO(
                sessao.getId(),
                sessao.getUsuario().getId(),
                sessao.getToken(),
                sessao.getIp(),
                sessao.getUserAgent(),
                sessao.getExpiraEm(),
                sessao.getCriadoEm()
        );
    }
}
