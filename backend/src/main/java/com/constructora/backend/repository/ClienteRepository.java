package com.constructora.backend.repository;


import com.constructora.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT c FROM Cliente c WHERE c.usuario.correoElectronico = :correo")
    Optional<Cliente> findByCorreoElectronico(String correo);
    
    boolean existsByUsuarioId(Long usuarioId);
}