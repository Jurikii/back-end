package com.juriki.api_juriki.auth.service;

import com.juriki.api_juriki.auth.dto.AuthResponseDTO;
import com.juriki.api_juriki.auth.dto.LoginRequestDTO;
import com.juriki.api_juriki.auth.dto.OAuthLoginResponseDTO;
import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.sessaousuario.model.SessaoUsuario;
import com.juriki.api_juriki.sessaousuario.repository.SessaoUsuarioRepository;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoUsuarioRepository sessaoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RecursoNaoEncontradoException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RecursoNaoEncontradoException("E-mail ou senha inválidos");
        }

        if (!usuario.isAtivo()) {
            throw new RecursoNaoEncontradoException("Conta inativa ou suspensa");
        }

        String token = jwtService.generateToken(usuario);
        criarSessao(usuario, token, httpRequest);

        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return new AuthResponseDTO(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                LocalDateTime.now().plusSeconds(604800)
        );
    }

    @Transactional
    public void logout(String token) {
        sessaoRepository.findByToken(token)
                .ifPresentOrElse(
                        sessao -> {
                            sessaoRepository.delete(sessao);
                            log.info("Sessão encerrada para token: {}", token.substring(0, Math.min(20, token.length())));
                        },
                        () -> {
                            log.warn("Tentativa de logout com token sem sessão ativa");
                            throw new RecursoNaoEncontradoException("Sessão não encontrada");
                        }
                );
    }

    @Transactional
    public OAuthLoginResponseDTO processarOAuthLogin(Usuario usuario, HttpServletRequest httpRequest) {
        String token = jwtService.generateToken(usuario);
        criarSessao(usuario, token, httpRequest);

        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return new OAuthLoginResponseDTO(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoPerfil(),
                false,
                LocalDateTime.now().plusSeconds(604800)
        );
    }

    private void criarSessao(Usuario usuario, String token, HttpServletRequest httpRequest) {
        SessaoUsuario sessao = SessaoUsuario.builder()
                .usuario(usuario)
                .token(token)
                .ip(obterIp(httpRequest))
                .userAgent(httpRequest.getHeader("User-Agent"))
                .expiraEm(LocalDateTime.now().plusSeconds(604800))
                .build();

        sessaoRepository.save(sessao);
        log.info("Sessão criada para usuário ID: {}", usuario.getId());
    }

    private String obterIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
