package com.constructora.backend.config;

import com.constructora.backend.entity.enums.TipoUsuario;
import com.constructora.backend.entity.Usuario;
import com.constructora.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo crea el admin si no existe
        if (usuarioRepository.findByCorreoElectronico("admin@constructora.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setCorreoElectronico("admin@constructora.com");
            admin.setContrasena(passwordEncoder.encode("Admin123!"));
            admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
            admin.setActivo(true);
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado!");
        } else {
            System.out.println("Usuario admin ya existe.");
        }
    }
}
