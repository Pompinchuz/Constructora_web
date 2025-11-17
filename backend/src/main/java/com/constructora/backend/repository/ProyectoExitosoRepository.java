package com.constructora.backend.repository;

import com.constructora.backend.entity.ProyectoExitoso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoExitosoRepository extends JpaRepository<ProyectoExitoso, Long> {
    
    List<ProyectoExitoso> findByActivoTrueOrderByFechaCreacionDesc();
    
    List<ProyectoExitoso> findAllByOrderByFechaCreacionDesc();
    
    @Query("SELECT p FROM ProyectoExitoso p WHERE p.activo = true AND p.fechaFinalizacion IS NOT NULL ORDER BY p.fechaFinalizacion DESC")
    List<ProyectoExitoso> findProyectosFinalizados();
    
    long countByActivoTrue();
}