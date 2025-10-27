// src/main/java/com/constructora/backend/service/impl/UsuarioServiceImpl.java
package com.constructora.backend.service.impl;

import com.constructora.backend.model.Usuario;
import com.constructora.backend.model.enums.Rol;
import com.constructora.backend.repository.UsuarioRepository;
import com.constructora.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder encoder;

    @Override
public Usuario registrarCliente(String nombre, String correo, String contrasena) {
    if (usuarioRepo.existsByCorreo(correo)) throw new IllegalArgumentException("Correo ya registrado");
    Usuario u = Usuario.builder()
            .nombreUsuario(nombre)
            .correo(correo)
            .contrasenaHash(encoder.encode(contrasena))
            .rol(Rol.CLIENTE)
            .build();
    return usuarioRepo.save(u);
}

@Override public Usuario buscarPorCorreo(String correo) {
    return usuarioRepo.findByCorreo(correo).orElse(null);
}

@Override public boolean existeAdmin() {
    return usuarioRepo.existsByRol(Rol.ADMIN);
}

@Override
public Usuario crearAdminSiNoExiste(String nombre, String correo, String contrasena) {
    return usuarioRepo.findByCorreo(correo).orElseGet(() -> {
        Usuario admin = Usuario.builder()
                .nombreUsuario(nombre)
                .correo(correo)
                .contrasenaHash(encoder.encode(contrasena))
                .rol(Rol.ADMIN)
                .build();
        return usuarioRepo.save(admin);
    });
}

}
