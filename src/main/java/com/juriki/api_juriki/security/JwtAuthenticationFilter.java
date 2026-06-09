package com.juriki.api_juriki.security;

import com.juriki.api_juriki.auth.security.UsuarioDetails;
import com.juriki.api_juriki.auth.service.JwtService;
import com.juriki.api_juriki.sessaousuario.repository.SessaoUsuarioRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SessaoUsuarioRepository sessaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            log.warn("JWT inválido ou expirado");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"mensagem\":\"Token inválido ou expirado\"}");
            return;
        }

        boolean sessaoAtiva = sessaoRepository.findByToken(token).isPresent();
        if (!sessaoAtiva) {
            log.warn("Nenhuma sessão ativa para o token informado");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"mensagem\":\"Sessão não encontrada ou expirada\"}");
            return;
        }

        String userId = jwtService.extractUserId(token);
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(userId)).orElse(null);

        if (usuario == null || !usuario.isAtivo()) {
            log.warn("Usuário não encontrado ou inativo: {}", userId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"mensagem\":\"Usuário não encontrado ou inativo\"}");
            return;
        }

        UsuarioDetails userDetails = new UsuarioDetails(usuario);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
