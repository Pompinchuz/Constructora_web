// src/main/java/com/constructora/backend/security/JwtAuthFilter.java
package com.constructora.backend.security;

import com.constructora.backend.model.Usuario;
import com.constructora.backend.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwt;
    private final UsuarioRepository usuarioRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims claims = jwt.parse(token).getBody();
                String correo = claims.getSubject();
                String rol = (String) claims.get("rol");
                Usuario u = usuarioRepo.findByCorreo(correo).orElse(null);
                if (u != null) {
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);
                    Authentication authToken =
                            new UsernamePasswordAuthenticationToken(correo, null, List.of(authority));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception ignored) { }
        }
        chain.doFilter(req, res);
    }
}
