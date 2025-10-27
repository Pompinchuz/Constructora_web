// src/main/java/com/constructora/backend/repository/UsuarioRepository.java
package com.constructora.backend.repository;

import com.constructora.backend.model.Usuario;
import com.constructora.backend.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Short> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByRol(Rol rol);
}