package com.constructora.backend.repository;

import com.constructora.backend.entity.ClientePersonaJuridica;
import com.constructora.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientePersonaJuridicaRepository extends JpaRepository<ClientePersonaJuridica, Long> {
    
    Optional<ClientePersonaJuridica> findByUsuario(Usuario usuario);
    
    Optional<ClientePersonaJuridica> findByRuc(String ruc);
    
    boolean existsByRuc(String ruc);
    
    Optional<ClientePersonaJuridica> findByUsuarioId(Long usuarioId);
}
