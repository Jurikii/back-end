package com.juriki.api_juriki.atendimento.service;

import com.juriki.api_juriki.atendimento.dto.AtendimentoRequestDTO;
import com.juriki.api_juriki.atendimento.dto.AtendimentoResponseDTO;
import com.juriki.api_juriki.atendimento.dto.AtendimentoStatusDTO;
import com.juriki.api_juriki.atendimento.enums.StatusAtendimento;
import com.juriki.api_juriki.atendimento.exception.AtendimentoNotFoundException;
import com.juriki.api_juriki.atendimento.exception.ConflitoAgendaException;
import com.juriki.api_juriki.atendimento.exception.StatusInvalidoException;
import com.juriki.api_juriki.atendimento.mapper.AtendimentoMapper;
import com.juriki.api_juriki.atendimento.model.Atendimento;
import com.juriki.api_juriki.atendimento.repository.AtendimentoRepository;
import com.juriki.api_juriki.advogado.model.Advogado;
import com.juriki.api_juriki.advogado.repository.AdvogadoRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final AdvogadoRepository advogadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtendimentoMapper mapper;

    private static final int DURACAO_CONSULTA_MINUTOS = 60;

    private static final java.util.Map<StatusAtendimento, Set<StatusAtendimento>> TRANSICOES_VALIDAS = java.util.Map.of(
            StatusAtendimento.AGUARDANDO_CONFIRMACAO, Set.of(StatusAtendimento.CONFIRMADO, StatusAtendimento.CANCELADO),
            StatusAtendimento.CONFIRMADO, Set.of(StatusAtendimento.EM_ANDAMENTO, StatusAtendimento.CANCELADO),
            StatusAtendimento.EM_ANDAMENTO, Set.of(StatusAtendimento.CONCLUIDO),
            StatusAtendimento.CONCLUIDO, Set.of(),
            StatusAtendimento.CANCELADO, Set.of()
    );

    @Transactional
    public AtendimentoResponseDTO agendar(Integer idUsuario, AtendimentoRequestDTO dto) {
        log.info("Agendando atendimento para usuário {} com advogado {}", idUsuario, dto.idAdvogado());

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        if (usuario.getPlano() == null) {
            throw new RuntimeException("Usuário não possui plano ativo. Assine um plano para agendar consultas.");
        }

        Advogado advogado = advogadoRepository.findById(dto.idAdvogado())
                .orElseThrow(() -> new RuntimeException("Advogado não encontrado: " + dto.idAdvogado()));

        LocalDateTime inicio = dto.dataConsulta();
        LocalDateTime fim = inicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        if (atendimentoRepository.existeConflito(dto.idAdvogado(), inicio, fim)) {
            throw new ConflitoAgendaException();
        }

        Atendimento atendimento = Atendimento.builder()
                .usuario(usuario)
                .advogado(advogado)
                .descricao(dto.descricao())
                .dataConsulta(dto.dataConsulta())
                .observacoes(dto.observacoes())
                .linkReuniao(dto.linkReuniao())
                .status(StatusAtendimento.AGUARDANDO_CONFIRMACAO)
                .build();

        atendimento = atendimentoRepository.save(atendimento);

        log.info("Atendimento ID {} agendado com sucesso.", atendimento.getId());

        return mapper.toResponseDTO(atendimento);
    }

    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return atendimentoRepository.findByUsuarioIdOrderByDataConsultaDesc(idUsuario)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> listarPorAdvogado(Integer idAdvogado) {
        return atendimentoRepository.findByAdvogadoIdOrderByDataConsultaDesc(idAdvogado)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtendimentoResponseDTO buscarPorId(Integer id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new AtendimentoNotFoundException(id));
        return mapper.toResponseDTO(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO atualizarStatus(Integer id, AtendimentoStatusDTO dto) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new AtendimentoNotFoundException(id));

        StatusAtendimento statusAtual = atendimento.getStatus();
        StatusAtendimento novoStatus = dto.status();

        Set<StatusAtendimento> transicoesPermitidas = TRANSICOES_VALIDAS.getOrDefault(statusAtual, Set.of());
        if (!transicoesPermitidas.contains(novoStatus)) {
            throw new StatusInvalidoException(statusAtual, novoStatus);
        }

        atendimento.setStatus(novoStatus);
        AtendimentoResponseDTO response = mapper.toResponseDTO(atendimentoRepository.save(atendimento));

        return response;
    }

    @Transactional
    public AtendimentoResponseDTO cancelar(Integer id) {
        return atualizarStatus(id, new AtendimentoStatusDTO(StatusAtendimento.CANCELADO));
    }

    @Transactional
    public AtendimentoResponseDTO finalizar(Integer id) {
        return atualizarStatus(id, new AtendimentoStatusDTO(StatusAtendimento.CONCLUIDO));
    }

    @Transactional(readOnly = true)
    public AtendimentoResponseDTO buscarProximoAtendimento(Integer idUsuario) {
        return atendimentoRepository
                .findProximoAtendimentoUsuario(idUsuario, LocalDateTime.now())
                .map(mapper::toResponseDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean isPrimeiraConsulta(Integer idUsuario, Integer idAdvogado) {
        return !atendimentoRepository.existsByUsuarioIdAndAdvogadoId(idUsuario, idAdvogado);
    }
}
