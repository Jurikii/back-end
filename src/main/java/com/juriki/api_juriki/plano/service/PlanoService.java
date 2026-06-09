package com.juriki.api_juriki.plano.service;

import com.juriki.api_juriki.exception.ConflitoException;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.plano.dto.PlanoAtualizarRequestDTO;
import com.juriki.api_juriki.plano.dto.PlanoCriarRequestDTO;
import com.juriki.api_juriki.plano.dto.PlanoResponseDTO;
import com.juriki.api_juriki.plano.model.Plano;
import com.juriki.api_juriki.plano.repository.PlanoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    // -------------------------------------------------------
    // CRIAR
    // -------------------------------------------------------

    @Transactional
    public PlanoResponseDTO criar(PlanoCriarRequestDTO request) {

        // Evita planos com nome duplicado
        boolean nomeExiste = planoRepository.findAll()
                .stream()
                .anyMatch(p -> p.getNomePlano().equalsIgnoreCase(request.getNomePlano()));

        if (nomeExiste) {
            throw new ConflitoException("Já existe um plano com o nome \"" + request.getNomePlano() + "\".");
        }

        Plano plano = Plano.builder()
                .nomePlano(request.getNomePlano())
                .descricao(request.getDescricao())
                .valor(request.getValor())
                .beneficios(request.getBeneficios())
                .limiteChatsMes(request.getLimiteChatsMes())
                .limiteUploads(request.getLimiteUploads())
                .acessoIaAvancada(request.getAcessoIaAvancada())
                .consultasIlimitadas(request.getConsultasIlimitadas())
                .ativo(true)
                .build();

        planoRepository.save(plano);

        log.info("Plano criado: {} | valor R$ {}", plano.getNomePlano(), plano.getValor());

        return toResponse(plano);
    }

    // -------------------------------------------------------
    // BUSCAR POR ID
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public PlanoResponseDTO buscarPorId(Integer idPlano) {
        return toResponse(buscarPlano(idPlano));
    }

    // -------------------------------------------------------
    // LISTAR TODOS
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<PlanoResponseDTO> listarTodos() {
        return planoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // LISTAR APENAS ATIVOS (para exibir no frontend para clientes)
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<PlanoResponseDTO> listarAtivos() {
        return planoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // ATUALIZAR
    // -------------------------------------------------------

    @Transactional
    public PlanoResponseDTO atualizar(Integer idPlano, PlanoAtualizarRequestDTO request) {

        Plano plano = buscarPlano(idPlano);

        if (request.getNomePlano() != null) {
            plano.setNomePlano(request.getNomePlano());
        }
        if (request.getDescricao() != null) {
            plano.setDescricao(request.getDescricao());
        }
        if (request.getValor() != null) {
            plano.setValor(request.getValor());
        }
        if (request.getBeneficios() != null) {
            plano.setBeneficios(request.getBeneficios());
        }
        if (request.getLimiteChatsMes() != null) {
            plano.setLimiteChatsMes(request.getLimiteChatsMes());
        }
        if (request.getLimiteUploads() != null) {
            plano.setLimiteUploads(request.getLimiteUploads());
        }
        if (request.getAcessoIaAvancada() != null) {
            plano.setAcessoIaAvancada(request.getAcessoIaAvancada());
        }
        if (request.getConsultasIlimitadas() != null) {
            plano.setConsultasIlimitadas(request.getConsultasIlimitadas());
        }
        if (request.getAtivo() != null) {
            plano.setAtivo(request.getAtivo());
        }

        planoRepository.save(plano);

        log.info("Plano ID {} atualizado.", idPlano);

        return toResponse(plano);
    }

    // -------------------------------------------------------
    // DELETAR
    // Não permite deletar se houver usuários vinculados ao plano
    // -------------------------------------------------------

    @Transactional
    public void deletar(Integer idPlano) {
        Plano plano = buscarPlano(idPlano);
        planoRepository.delete(plano);
        log.info("Plano ID {} removido.", idPlano);
    }

    // -------------------------------------------------------
    // HELPERS PRIVADOS
    // -------------------------------------------------------

    private Plano buscarPlano(Integer idPlano) {
        return planoRepository.findById(idPlano)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado."));
    }

    private PlanoResponseDTO toResponse(Plano plano) {
        return PlanoResponseDTO.builder()
                .idPlano(plano.getId())
                .nomePlano(plano.getNomePlano())
                .descricao(plano.getDescricao())
                .valor(plano.getValor())
                .beneficios(plano.getBeneficios())
                .limiteChatsMes(plano.getLimiteChatsMes())
                .limiteUploads(plano.getLimiteUploads())
                .acessoIaAvancada(plano.getAcessoIaAvancada())
                .consultasIlimitadas(plano.getConsultasIlimitadas())
                .ativo(plano.getAtivo())
                .criadoEm(plano.getCriadoEm())
                .build();
    }
}
