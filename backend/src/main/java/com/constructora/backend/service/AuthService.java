package com.constructora.backend.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.constructora.backend.controller.dto.ClientePersonaJuridicaResponseDTO;
import com.constructora.backend.controller.dto.ClientePersonaNaturalResponseDTO;
import com.constructora.backend.controller.dto.auth.LoginRequestDTO;
import com.constructora.backend.controller.dto.auth.LoginResponseDTO;
import com.constructora.backend.controller.dto.auth.RegistroPersonaJuridicaDTO;
import com.constructora.backend.controller.dto.auth.RegistroPersonaNaturalDTO;
import com.constructora.backend.controller.dto.cliente.ClienteResponseDTO;
import com.constructora.backend.entity.ClientePersonaJuridica;
import com.constructora.backend.entity.ClientePersonaNatural;
import com.constructora.backend.entity.Usuario;
import com.constructora.backend.entity.enums.TipoUsuario;
import com.constructora.backend.exception.ConflictException;
import com.constructora.backend.exception.UnauthorizedException;
import com.constructora.backend.repository.ClientePersonaJuridicaRepository;
import com.constructora.backend.repository.ClientePersonaNaturalRepository;
import com.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final ClientePersonaNaturalRepository clienteNaturalRepository;
    private final ClientePersonaJuridicaRepository clienteJuridicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    
    @Transactional
    public ClienteResponseDTO registrarPersonaNatural(RegistroPersonaNaturalDTO dto) {
        validarCorreoUnico(dto.getCorreoElectronico());
        
        if (dto.getDni() != null) {
            validarDniUnico(dto.getDni());
        }
        
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setTipoUsuario(TipoUsuario.CLIENTE_NATURAL);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);
        
        // Crear cliente persona natural
        ClientePersonaNatural cliente = new ClientePersonaNatural();
        cliente.setUsuario(usuario);
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setDni(dto.getDni());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente = clienteNaturalRepository.save(cliente);
        
        // Enviar correo de bienvenida
        try {
            emailService.enviarCorreoBienvenida(usuario.getCorreoElectronico(), 
                    cliente.getNombreCompleto());
        } catch (Exception e) {
            log.warn("Error enviando correo de bienvenida: {}", e.getMessage());
        }
        
        return mapearClienteNaturalAResponse(cliente);
    }
    
    @Transactional
    public ClienteResponseDTO registrarPersonaJuridica(RegistroPersonaJuridicaDTO dto) {
        validarCorreoUnico(dto.getCorreoElectronico());
        validarRucUnico(dto.getRuc());
        
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setTipoUsuario(TipoUsuario.CLIENTE_JURIDICO);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);
        
        // Crear cliente persona jurídica
        ClientePersonaJuridica cliente = new ClientePersonaJuridica();
        cliente.setUsuario(usuario);
        cliente.setRazonSocial(dto.getRazonSocial());
        cliente.setRuc(dto.getRuc());
        cliente.setRepresentanteLegal(dto.getRepresentanteLegal());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente = clienteJuridicoRepository.save(cliente);
        
        // Enviar correo de bienvenida
        try {
            emailService.enviarCorreoBienvenida(usuario.getCorreoElectronico(), 
                    cliente.getNombreCompleto());
        } catch (Exception e) {
            log.warn("Error enviando correo de bienvenida: {}", e.getMessage());
        }
        
        return mapearClienteJuridicoAResponse(cliente);
    }
    
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Intento de login para: {}", dto.getCorreoElectronico());
        
        // Buscar usuario
        Usuario usuario = usuarioRepository.findByCorreoElectronico(dto.getCorreoElectronico())
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", dto.getCorreoElectronico());
                    return new UnauthorizedException("Credenciales inválidas");
                });
        
        log.debug("Usuario encontrado - ID: {}", usuario.getId());
        log.debug("Usuario activo: {}", usuario.getActivo());
        
        // Verificar que el usuario esté activo
        if (!usuario.getActivo()) {
            log.warn("Usuario inactivo: {}", dto.getCorreoElectronico());
            throw new UnauthorizedException("Usuario inactivo");
        }
        
        // Verificar contraseña
        boolean matches = passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena());
        log.debug("¿Contraseña coincide?: {}", matches);
        
        if (!matches) {
            log.error("Contraseña incorrecta para: {}", dto.getCorreoElectronico());
            throw new UnauthorizedException("Credenciales inválidas");
        }
        
        log.info("Autenticación exitosa para: {}", dto.getCorreoElectronico());
        
        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        // Obtener nombre completo
        String nombreCompleto;
        try {
            nombreCompleto = obtenerNombreCompleto(usuario);
            log.debug("Nombre completo obtenido: {}", nombreCompleto);
        } catch (Exception e) {
            log.error("Error obteniendo nombre completo: {}", e.getMessage());
            nombreCompleto = "Usuario";
        }
        
        // Generar token JWT
        String token;
        try {
            token = jwtService.generarToken(usuario);
            log.debug("Token JWT generado exitosamente");
        } catch (Exception e) {
            log.error("Error generando token JWT: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar token de autenticación");
        }
        
        // Construir respuesta
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .expiraEn(jwtService.getExpiracion())
                .correoElectronico(usuario.getCorreoElectronico())
                .tipoUsuario(usuario.getTipoUsuario())
                .nombreCompleto(nombreCompleto)
                .build();
        
        log.info("Login completado exitosamente para: {}", dto.getCorreoElectronico());
        
        return response;
    }
    
    private void validarCorreoUnico(String correo) {
        if (usuarioRepository.existsByCorreoElectronico(correo)) {
            throw new ConflictException("El correo electrónico ya está registrado");
        }
    }
    
    private void validarDniUnico(String dni) {
        if (clienteNaturalRepository.existsByDni(dni)) {
            throw new ConflictException("El DNI ya está registrado");
        }
    }
    
    private void validarRucUnico(String ruc) {
        if (clienteJuridicoRepository.existsByRuc(ruc)) {
            throw new ConflictException("El RUC ya está registrado");
        }
    }
    
    private String obtenerNombreCompleto(Usuario usuario) {
        log.debug("Obteniendo nombre para tipo: {}", usuario.getTipoUsuario());
        
        return switch (usuario.getTipoUsuario()) {
            case CLIENTE_NATURAL -> {
                var cliente = clienteNaturalRepository.findByUsuario(usuario)
                        .orElseThrow(() -> new RuntimeException("Cliente natural no encontrado"));
                yield cliente.getNombreCompleto();
            }
            case CLIENTE_JURIDICO -> {
                var cliente = clienteJuridicoRepository.findByUsuario(usuario)
                        .orElseThrow(() -> new RuntimeException("Cliente jurídico no encontrado"));
                yield cliente.getNombreCompleto();
            }
            case ADMINISTRADOR -> "Administrador";
        };
    }
    
    // ============================================
    // MÉTODOS DE VALIDACIÓN
    // ============================================
    
    public boolean esCorreoDisponible(String correo) {
        return !usuarioRepository.existsByCorreoElectronico(correo);
    }
    
    public boolean esDniDisponible(String dni) {
        return !clienteNaturalRepository.existsByDni(dni);
    }
    
    public boolean esRucDisponible(String ruc) {
        return !clienteJuridicoRepository.existsByRuc(ruc);
    }
    
    // ============================================
    // MÉTODOS DE MAPEO
    // ============================================
    
    private ClientePersonaNaturalResponseDTO mapearClienteNaturalAResponse(ClientePersonaNatural cliente) {
        ClientePersonaNaturalResponseDTO dto = new ClientePersonaNaturalResponseDTO();
        dto.setId(cliente.getId());
        dto.setCorreoElectronico(cliente.getUsuario().getCorreoElectronico());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setActivo(cliente.getUsuario().getActivo());
        dto.setNombres(cliente.getNombres());
        dto.setApellidos(cliente.getApellidos());
        dto.setDni(cliente.getDni());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        return dto;
    }
    
    private ClientePersonaJuridicaResponseDTO mapearClienteJuridicoAResponse(ClientePersonaJuridica cliente) {
        ClientePersonaJuridicaResponseDTO dto = new ClientePersonaJuridicaResponseDTO();
        dto.setId(cliente.getId());
        dto.setCorreoElectronico(cliente.getUsuario().getCorreoElectronico());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setActivo(cliente.getUsuario().getActivo());
        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setRuc(cliente.getRuc());
        dto.setRepresentanteLegal(cliente.getRepresentanteLegal());
        return dto;
    }
}