package com.constructora.backend.repository;

import com.constructora.backend.entity.ClientePersonaNatural;
import com.constructora.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientePersonaNaturalRepository extends JpaRepository<ClientePersonaNatural, Long> {
    
    Optional<ClientePersonaNatural> findByUsuario(Usuario usuario);
    
    Optional<ClientePersonaNatural> findByDni(String dni);
    
    boolean existsByDni(String dni);
    
    Optional<ClientePersonaNatural> findByUsuarioId(Long usuarioId);
}