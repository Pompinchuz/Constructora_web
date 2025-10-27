// src/main/java/com/constructora/backend/controller/AuthController.java
package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.*;
import com.constructora.backend.controller.dto.AuthDtos.AuthResponse;
import com.constructora.backend.controller.dto.AuthDtos.LoginRequest;
import com.constructora.backend.controller.dto.AuthDtos.RegisterRequest;
import com.constructora.backend.model.Usuario;
import com.constructora.backend.security.JwtUtil;
import com.constructora.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        Usuario u = usuarioService.registrarCliente(req.nombre(), req.correo(), req.password());
        String token = jwt.generate(u.getCorreo(), Map.of("rol", u.getRol().name()));
        return ResponseEntity.ok(new AuthResponse(token, u.getRol().name(), u.getCorreo(), u.getNombreUsuario()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        Usuario u = usuarioService.buscarPorCorreo(req.correo());
        if (u == null || !encoder.matches(req.password(), u.getContrasenaHash()))
            return ResponseEntity.status(401).build();
        String token = jwt.generate(u.getCorreo(), Map.of("rol", u.getRol().name()));
        return ResponseEntity.ok(new AuthResponse(token, u.getRol().name(), u.getCorreo(), u.getNombreUsuario()));
    }
}
