package com.constructora.backend.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        emailService.enviarCorreoBienvenida(usuario.getCorreoElectronico(), 
                cliente.getNombreCompleto());
        
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
        emailService.enviarCorreoBienvenida(usuario.getCorreoElectronico(), 
                cliente.getNombreCompleto());
        
        return mapearClienteJuridicoAResponse(cliente);
    }
    
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(dto.getCorreoElectronico())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));
        
        if (!usuario.getActivo()) {
            throw new UnauthorizedException("Usuario inactivo");
        }
        
        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
        
        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        // Generar token JWT
        String token = jwtService.generarToken(usuario);
        String nombreCompleto = obtenerNombreCompleto(usuario);
        
        return LoginResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .expiraEn(jwtService.getExpiracion())
                .correoElectronico(usuario.getCorreoElectronico())
                .tipoUsuario(usuario.getTipoUsuario())
                .nombreCompleto(nombreCompleto)
                .build();
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
        // Lógica para obtener nombre según tipo de usuario
        return switch (usuario.getTipoUsuario()) {
            case CLIENTE_NATURAL -> clienteNaturalRepository.findByUsuario(usuario)
                    .map(ClientePersonaNatural::getNombreCompleto).orElse("");
            case CLIENTE_JURIDICO -> clienteJuridicoRepository.findByUsuario(usuario)
                    .map(ClientePersonaJuridica::getNombreCompleto).orElse("");
            case ADMINISTRADOR -> "Administrador";
        };
    }
    
    // Métodos de mapeo...
}
