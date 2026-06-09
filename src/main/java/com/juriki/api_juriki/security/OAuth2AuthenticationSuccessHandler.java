package com.juriki.api_juriki.security;

import com.juriki.api_juriki.auth.dto.OAuthLoginResponseDTO;
import com.juriki.api_juriki.auth.service.AuthService;
import com.juriki.api_juriki.auth.service.JwtService;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Autenticação OAuth2 inválida");
            return;
        }

        OAuth2User oauthUser = oauthToken.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();

        String email = (String) attributes.get("email");
        String nome = (String) attributes.get("name");
        String fotoPerfil = (String) attributes.get("picture");

        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "E-mail não fornecido pelo Google");
            return;
        }

        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario novoUsuario = Usuario.builder()
                    .nome(nome != null ? nome : email.split("@")[0])
                    .email(email)
                    .senha("")
                    .fotoPerfil(fotoPerfil)
                    .tipoUsuario(ETipoUsuario.CLIENTE)
                    .emailVerificado(true)
                    .build();
            return usuarioRepository.save(novoUsuario);
        });

        OAuthLoginResponseDTO authResponse = authService.processarOAuthLogin(usuario, request);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(formatarResposta(authResponse));
    }

    private String formatarResposta(OAuthLoginResponseDTO dto) {
        return String.format(
                "{\"token\":\"%s\",\"tipo\":\"%s\",\"idUsuario\":%d,\"nome\":\"%s\",\"email\":\"%s\",\"fotoPerfil\":%s,\"novoUsuario\":%b,\"expiraEm\":\"%s\"}",
                dto.token(),
                dto.tipo(),
                dto.idUsuario(),
                dto.nome() != null ? dto.nome().replace("\"", "\\\"") : "",
                dto.email() != null ? dto.email().replace("\"", "\\\"") : "",
                dto.fotoPerfil() != null ? "\"" + dto.fotoPerfil().replace("\"", "\\\"") + "\"" : "null",
                dto.novoUsuario(),
                dto.expiraEm() != null ? dto.expiraEm().toString() : ""
        );
    }
}
