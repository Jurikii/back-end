package com.juriki.api_juriki.configuracao_ia.service;

import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaAtualizarRequestDTO;
import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaCriarRequestDTO;
import com.juriki.api_juriki.configuracao_ia.dto.ConfiguracaoIaResponseDTO;
import com.juriki.api_juriki.configuracao_ia.model.ConfiguracaoIa;
import com.juriki.api_juriki.configuracao_ia.repository.ConfiguracaoIaRepository;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracaoIaService {

    private final ConfiguracaoIaRepository configuracaoIaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ConfiguracaoIaResponseDTO criar(ConfiguracaoIaCriarRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        ConfiguracaoIa config = ConfiguracaoIa.builder()
                .usuario(usuario)
                .modeloPadrao(request.getModeloPadrao())
                .temperatura(request.getTemperatura())
                .respostasLongas(request.getRespostasLongas())
                .linguagemJuridica(request.getLinguagemJuridica())
                .simplificarTermos(request.getSimplificarTermos())
                .build();

        configuracaoIaRepository.save(config);

        log.info("Configuração IA criada para usuário ID {}", usuario.getId());

        return toResponse(config);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoIaResponseDTO buscarPorId(Integer idConfig) {
        return toResponse(buscarConfig(idConfig));
    }

    @Transactional(readOnly = true)
    public List<ConfiguracaoIaResponseDTO> listarTodos() {
        return configuracaoIaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConfiguracaoIaResponseDTO buscarPorUsuario(Integer idUsuario) {
        return configuracaoIaRepository.findByUsuarioId(idUsuario)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Configuração IA não encontrada para o usuário."));
    }

    @Transactional
    public ConfiguracaoIaResponseDTO atualizar(Integer idConfig, ConfiguracaoIaAtualizarRequestDTO request) {
        ConfiguracaoIa config = buscarConfig(idConfig);

        if (request.getModeloPadrao() != null) {
            config.setModeloPadrao(request.getModeloPadrao());
        }
        if (request.getTemperatura() != null) {
            config.setTemperatura(request.getTemperatura());
        }
        if (request.getRespostasLongas() != null) {
            config.setRespostasLongas(request.getRespostasLongas());
        }
        if (request.getLinguagemJuridica() != null) {
            config.setLinguagemJuridica(request.getLinguagemJuridica());
        }
        if (request.getSimplificarTermos() != null) {
            config.setSimplificarTermos(request.getSimplificarTermos());
        }

        configuracaoIaRepository.save(config);

        log.info("Configuração IA ID {} atualizada.", idConfig);

        return toResponse(config);
    }

    @Transactional
    public void deletar(Integer idConfig) {
        ConfiguracaoIa config = buscarConfig(idConfig);
        configuracaoIaRepository.delete(config);
        log.info("Configuração IA ID {} removida.", idConfig);
    }

    private ConfiguracaoIa buscarConfig(Integer idConfig) {
        return configuracaoIaRepository.findById(idConfig)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Configuração IA não encontrada."));
    }

    private ConfiguracaoIaResponseDTO toResponse(ConfiguracaoIa config) {
        return ConfiguracaoIaResponseDTO.builder()
                .idConfig(config.getId())
                .idUsuario(config.getUsuario().getId())
                .modeloPadrao(config.getModeloPadrao())
                .temperatura(config.getTemperatura())
                .respostasLongas(config.getRespostasLongas())
                .linguagemJuridica(config.getLinguagemJuridica())
                .simplificarTermos(config.getSimplificarTermos())
                .criadoEm(config.getCriadoEm())
                .build();
    }
}
