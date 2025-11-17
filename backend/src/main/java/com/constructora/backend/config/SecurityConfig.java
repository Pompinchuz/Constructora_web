package com.constructora.backend.config;

import com.constructora.backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CorsConfig corsConfig;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // ============================================
                // RUTAS PÚBLICAS (sin autenticación)
                // ============================================
                .requestMatchers(
                    // Auth
                    "/api/auth/**",
                    
                    // Contenido público - CON /api/
                    "/api/contenido/imagenes/publico/**",
                    "/api/contenido/imagenes/activas/**",
                    "/api/contenido/imagenes/tipo/*/activas",
                    "/api/contenido/proyectos/publico/**",
                    "/api/contenido/proyectos/activos",
                    
                    // Contenido público - SIN /api/ (por si acaso)
                    "/contenido/imagenes/publico/**",
                    "/contenido/proyectos/publico/**",
                    "/contenido/proyectos/activos",
                    
                    // Archivos estáticos
                    "/api/files/public/**",
                    "/uploads/**",
                    "/files/**",
                    
                    // Otros
                    "/error",
                    "/actuator/health"
                ).permitAll()
                
                // ============================================
                // RUTAS DE ADMINISTRADOR
                // ============================================
                .requestMatchers(
                    "/api/admin/**",
                    "/api/*/admin/**",
                    "/api/contenido/imagenes",
                    "/api/contenido/imagenes/**",
                    "/api/contenido/proyectos",
                    "/api/contenido/proyectos/**"
                ).hasAuthority("ADMINISTRADOR")
                
                // ============================================
                // RUTAS DE CLIENTE
                // ============================================
                .requestMatchers(
                    "/api/solicitudes/mis-solicitudes",
                    "/api/proformas/mis-proformas",
                    "/api/comprobantes/mis-comprobantes",
                    "/api/cliente/**"
                ).hasAnyAuthority("CLIENTE_NATURAL", "CLIENTE_JURIDICO")
                
                // ============================================
                // RESTO DE RUTAS REQUIEREN AUTENTICACIÓN
                // ============================================
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
            throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}