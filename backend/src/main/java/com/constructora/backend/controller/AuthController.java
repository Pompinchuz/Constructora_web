// AuthController.java
package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.cliente.ClienteResponseDTO;
import com.constructora.backend.controller.dto.auth.LoginRequestDTO;
import com.constructora.backend.controller.dto.auth.LoginResponseDTO;
import com.constructora.backend.controller.dto.auth.RegistroPersonaJuridicaDTO;
import com.constructora.backend.controller.dto.auth.RegistroPersonaNaturalDTO;
import com.constructora.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Login de usuario
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        
        log.info("Intento de login para: {}", request.getCorreoElectronico());
        
        LoginResponseDTO response = authService.login(request);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<LoginResponseDTO>builder()
                .success(true)
                .message("Login exitoso")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Registro de Persona Natural
     * POST /api/auth/registro/persona-natural
     */
    @PostMapping("/registro/persona-natural")
    public ResponseEntity<ApiResponseDTO<ClienteResponseDTO>> registrarPersonaNatural(
            @Valid @RequestBody RegistroPersonaNaturalDTO request) {
        
        log.info("Registro de persona natural: {}", request.getCorreoElectronico());
        
        ClienteResponseDTO response = authService.registrarPersonaNatural(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<ClienteResponseDTO>builder()
                .success(true)
                .message("Cliente registrado exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Registro de Persona Jurídica
     * POST /api/auth/registro/persona-juridica
     */
    @PostMapping("/registro/persona-juridica")
    public ResponseEntity<ApiResponseDTO<ClienteResponseDTO>> registrarPersonaJuridica(
            @Valid @RequestBody RegistroPersonaJuridicaDTO request) {
        
        log.info("Registro de persona jurídica: {}", request.getCorreoElectronico());
        
        ClienteResponseDTO response = authService.registrarPersonaJuridica(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<ClienteResponseDTO>builder()
                .success(true)
                .message("Empresa registrada exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Validar token JWT
     * GET /api/auth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponseDTO<Boolean>> validarToken(
            @RequestHeader("Authorization") String token) {
        
        // El token ya fue validado por el filtro JWT
        // Si llega aquí, es válido
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message("Token válido")
                .data(true)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Verificar disponibilidad de correo
     * GET /api/auth/check-email?email=correo@ejemplo.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarCorreoDisponible(
            @RequestParam String email) {
        
        boolean disponible = authService.esCorreoDisponible(email);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(disponible ? "Correo disponible" : "Correo no disponible")
                .data(disponible)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Verificar disponibilidad de DNI
     * GET /api/auth/check-dni?dni=12345678
     */
    @GetMapping("/check-dni")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarDniDisponible(
            @RequestParam String dni) {
        
        boolean disponible = authService.esDniDisponible(dni);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(disponible ? "DNI disponible" : "DNI no disponible")
                .data(disponible)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Verificar disponibilidad de RUC
     * GET /api/auth/check-ruc?ruc=12345678901
     */
    @GetMapping("/check-ruc")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarRucDisponible(
            @RequestParam String ruc) {
        
        boolean disponible = authService.esRucDisponible(ruc);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(disponible ? "RUC disponible" : "RUC no disponible")
                .data(disponible)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}