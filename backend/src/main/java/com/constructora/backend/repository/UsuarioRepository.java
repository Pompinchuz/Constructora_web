package com.constructora.backend.repository;


import com.constructora.backend.entity.Usuario;
import com.constructora.backend.entity.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    boolean existsByCorreoElectronico(String correoElectronico);
    
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    
    List<Usuario> findByActivoTrue();
    
    Optional<Usuario> findByCorreoElectronicoAndActivoTrue(String correoElectronico);
}