package com.juriki.api_juriki.advogado.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juriki.api_juriki.advogado.dto.AdvogadoAtualizarRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoCadastroCompletoRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoCriarRequestDTO;
import com.juriki.api_juriki.advogado.dto.AdvogadoResponseDTO;
import com.juriki.api_juriki.advogado.enums.EResultadoValidacaoOab;
import com.juriki.api_juriki.advogado.model.Advogado;
import com.juriki.api_juriki.advogado.repository.AdvogadoRepository;
import com.juriki.api_juriki.exception.ConflitoException;
import com.juriki.api_juriki.exception.OabCaptchaException;
import com.juriki.api_juriki.exception.OabInvalidaException;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.usuario.enums.EStatusConta;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvogadoService {

    private final AdvogadoRepository advogadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OabScrapingService oabScrapingService;
    private final PasswordEncoder passwordEncoder;

    // -------------------------------------------------------
    // CADASTRAR
    // -------------------------------------------------------

    @Transactional
    public AdvogadoResponseDTO criar(AdvogadoCriarRequestDTO request) {

        // 1. Busca o usuário vinculado (deve estar ativo)
        Usuario usuario = usuarioRepository
                .findByIdAndStatusContaAndDeletadoEmIsNull(request.getIdUsuario(), EStatusConta.ATIVA)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado ou não está ativo."));

        // 2. Garante que o usuário ainda não é advogado cadastrado
        if (advogadoRepository.existsByUsuarioId(request.getIdUsuario())) {
            throw new ConflitoException("Este usuário já possui um cadastro de advogado.");
        }

        // 3. Garante que OAB + estado não estejam duplicados
        String estadoUpper = request.getEstado().toUpperCase();
        if (advogadoRepository.existsByOabAndEstado(request.getOab(), estadoUpper)) {
            throw new ConflitoException("Já existe um advogado cadastrado com esta OAB e estado.");
        }

        // 4. Valida a OAB no site do CNA/OAB via Selenium
        validarOabOuLancarExcecao(request.getOab(), estadoUpper);

        // 5. Atualiza o tipo do usuário para ADVOGADO
        usuario.setTipoUsuario(ETipoUsuario.ADVOGADO);
        usuarioRepository.save(usuario);

        // 6. Monta e salva o advogado
        Advogado advogado = Advogado.builder()
                .usuario(usuario)
                .oab(request.getOab())
                .estado(estadoUpper)
                .especialidade(request.getEspecialidade())
                .biografia(request.getBiografia())
                .valorConsulta(request.getValorConsulta())
                .experienciaAnos(request.getExperienciaAnos())
                .linkedin(request.getLinkedin())
                .siteProfissional(request.getSiteProfissional())
                // aprovado = false por padrão: um admin precisa aprovar depois
                .aprovado(false)
                .build();

        advogadoRepository.save(advogado);

        log.info("Advogado cadastrado: OAB {} - {} | usuário ID {}",
                advogado.getOab(), advogado.getEstado(), usuario.getId());

        return toResponse(advogado);
    }

    // -------------------------------------------------------
    // CADASTRO COMPLETO (usuário + advogado)
    // -------------------------------------------------------

    @Transactional
    public AdvogadoResponseDTO cadastroCompleto(AdvogadoCadastroCompletoRequestDTO request) {

        // 1. Valida OAB primeiro — se falhar, nada é criado
        String estadoUpper = request.getEstado().toUpperCase();
        validarOabOuLancarExcecao(request.getOab(), estadoUpper);

        // 2. Valida e-mail duplicado
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflitoException("Já existe uma conta cadastrada com este e-mail.");
        }

        // 3. Valida CPF duplicado (se informado)
        if (request.getCpf() != null && usuarioRepository.existsByCpf(request.getCpf())) {
            throw new ConflitoException("Já existe uma conta cadastrada com este CPF.");
        }

        // 4. Cria o usuário como ADVOGADO
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .dataNascimento(request.getDataNascimento())
                .tipoUsuario(ETipoUsuario.ADVOGADO)
                .statusConta(EStatusConta.ATIVA)
                .build();

        usuarioRepository.save(usuario);

        // 5. Cria o advogado vinculado
        Advogado advogado = Advogado.builder()
                .usuario(usuario)
                .oab(request.getOab())
                .estado(estadoUpper)
                .especialidade(request.getEspecialidade())
                .biografia(request.getBiografia())
                .valorConsulta(request.getValorConsulta())
                .experienciaAnos(request.getExperienciaAnos())
                .linkedin(request.getLinkedin())
                .siteProfissional(request.getSiteProfissional())
                .aprovado(false)
                .build();

        advogadoRepository.save(advogado);

        log.info("Cadastro completo realizado: usuário ID {} | OAB {} - {}",
                usuario.getId(), advogado.getOab(), advogado.getEstado());

        return toResponse(advogado);
    }

    // -------------------------------------------------------
    // BUSCAR POR ID
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public AdvogadoResponseDTO buscarPorId(Integer idAdvogado) {
        Advogado advogado = buscarAdvogado(idAdvogado);
        return toResponse(advogado);
    }

    // -------------------------------------------------------
    // LISTAR TODOS
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<AdvogadoResponseDTO> listarTodos() {
        return advogadoRepository.findAllByUsuarioDeletadoEmIsNull()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // LISTAR APROVADOS
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<AdvogadoResponseDTO> listarAprovados() {
        return advogadoRepository.findByAprovadoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // BUSCAR POR ESPECIALIDADE
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<AdvogadoResponseDTO> buscarPorEspecialidade(String especialidade) {
        return advogadoRepository
                .findByEspecialidadeContainingIgnoreCaseAndAprovadoTrue(especialidade)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // -------------------------------------------------------
    // ATUALIZAR
    // -------------------------------------------------------

    @Transactional
    public AdvogadoResponseDTO atualizar(Integer idAdvogado, AdvogadoAtualizarRequestDTO request) {

        Advogado advogado = buscarAdvogado(idAdvogado);

        if (request.getEspecialidade() != null) {
            advogado.setEspecialidade(request.getEspecialidade());
        }
        if (request.getBiografia() != null) {
            advogado.setBiografia(request.getBiografia());
        }
        if (request.getValorConsulta() != null) {
            advogado.setValorConsulta(request.getValorConsulta());
        }
        if (request.getExperienciaAnos() != null) {
            advogado.setExperienciaAnos(request.getExperienciaAnos());
        }
        if (request.getLinkedin() != null) {
            advogado.setLinkedin(request.getLinkedin());
        }
        if (request.getSiteProfissional() != null) {
            advogado.setSiteProfissional(request.getSiteProfissional());
        }

        advogadoRepository.save(advogado);

        return toResponse(advogado);
    }

    // -------------------------------------------------------
    // APROVAR (ação de admin)
    // -------------------------------------------------------

    @Transactional
    public AdvogadoResponseDTO aprovar(Integer idAdvogado) {
        Advogado advogado = buscarAdvogado(idAdvogado);
        advogado.setAprovado(true);
        advogadoRepository.save(advogado);

        log.info("Advogado ID {} aprovado na plataforma.", idAdvogado);

        return toResponse(advogado);
    }

    // -------------------------------------------------------
    // DELETAR
    // -------------------------------------------------------

    @Transactional
    public void deletar(Integer idAdvogado) {

        Advogado advogado = buscarAdvogado(idAdvogado);

        Usuario usuario = advogado.getUsuario();
        usuario.setDeletadoEm(LocalDateTime.now());
        usuario.setStatusConta(EStatusConta.INATIVA);

        usuarioRepository.save(usuario);

        log.info("Usuário do advogado ID {} desativado.", idAdvogado);
    }
    // -------------------------------------------------------
    // HELPERS PRIVADOS
    // -------------------------------------------------------

    private void validarOabOuLancarExcecao(String oab, String estado) {
        log.info("Iniciando validação da OAB {} - {}", oab, estado);

        EResultadoValidacaoOab resultado = oabScrapingService.validarOab(oab, estado);

        switch (resultado) {
            case VALIDO -> log.info("OAB {} - {} validada com sucesso.", oab, estado);
            case INVALIDO -> throw new OabInvalidaException(
                    "A OAB informada não é válida ou não está com situação regular. " +
                    "Verifique o número e o estado e tente novamente."
            );
            case CAPTCHA -> throw new OabCaptchaException(
                    "Não foi possível validar sua OAB agora por instabilidade no site da OAB. " +
                    "Tente novamente em alguns minutos."
            );
            case ERRO -> throw new OabInvalidaException(
                    "Ocorreu um erro ao consultar a OAB. Tente novamente mais tarde."
            );
        }
    }

    private Advogado buscarAdvogado(Integer idAdvogado) {
        return advogadoRepository.findByIdAndUsuarioDeletadoEmIsNull(idAdvogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Advogado não encontrado."));
    }

    private AdvogadoResponseDTO toResponse(Advogado advogado) {
        Usuario u = advogado.getUsuario();

        return AdvogadoResponseDTO.builder()
                .idAdvogado(advogado.getId())
                .idUsuario(u.getId())
                .nomeUsuario(u.getNome())
                .emailUsuario(u.getEmail())
                .telefoneUsuario(u.getTelefone())
                .fotoPerfilUsuario(u.getFotoPerfil())
                .oab(advogado.getOab())
                .estado(advogado.getEstado())
                .especialidade(advogado.getEspecialidade())
                .biografia(advogado.getBiografia())
                .valorConsulta(advogado.getValorConsulta())
                .experienciaAnos(advogado.getExperienciaAnos())
                .linkedin(advogado.getLinkedin())
                .siteProfissional(advogado.getSiteProfissional())
                .aprovado(advogado.getAprovado())
                .statusProfissional(advogado.getStatusProfissional())
                .notaMedia(advogado.getNotaMedia())
                .totalAvaliacoes(advogado.getTotalAvaliacoes())
                .criadoEm(advogado.getCriadoEm())
                .build();
    }
}