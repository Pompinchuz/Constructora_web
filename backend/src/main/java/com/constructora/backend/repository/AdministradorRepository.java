package com.constructora.backend.repository;

import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    
    Optional<Administrador> findByUsuario(Usuario usuario);
    
    Optional<Administrador> findByUsuarioId(Long usuarioId);

    Optional<Administrador> findByUsuario_CorreoElectronico(String correoElectronico);
    /*
     * 
     * // ✅ Este método es el que se usa en obtenerAdminId()
    Optional<Administrador> findByUsuarioId(Long usuarioId);
    
    // Opcional: buscar por email del usuario
    Optional<Administrador> findByUsuario_CorreoElectronico(String correoElectronico);
     * 
     */
    
    boolean existsByUsuarioId(Long usuarioId);
}