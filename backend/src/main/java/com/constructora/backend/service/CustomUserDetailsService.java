// ============================================
// CUSTOM USER DETAILS SERVICE - CORREGIDO
// ============================================

package com.constructora.backend.service;

import com.constructora.backend.entity.Usuario;
import com.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando usuario por email: {}", username);
        
        Usuario usuario = usuarioRepository.findByCorreoElectronico(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });
        
        // ✅ IMPORTANTE: Crear las authorities correctamente
        Collection<GrantedAuthority> authorities = getAuthorities(usuario);
        
        log.debug("Usuario encontrado: {} con authorities: {}", username, authorities);
        
        return User.builder()
                .username(usuario.getCorreoElectronico())
                .password(usuario.getContrasena())
                .authorities(authorities)
                .accountLocked(!usuario.getActivo())
                .disabled(!usuario.getActivo())
                .build();
    }
    
    /**
     * Convierte el tipo de usuario en authorities de Spring Security
     * ✅ SIN el prefijo "ROLE_" porque usamos hasAuthority() en vez de hasRole()
     */
    private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Agregar el tipo de usuario como authority
        // IMPORTANTE: Sin el prefijo "ROLE_" porque usamos hasAuthority()
        String authority = usuario.getTipoUsuario().name();
        authorities.add(new SimpleGrantedAuthority(authority));
        
        log.debug("Authority agregada: {}", authority);
        
        return authorities;
    }
}