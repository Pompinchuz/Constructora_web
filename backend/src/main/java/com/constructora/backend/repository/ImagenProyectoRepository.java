package com.constructora.backend.repository;

import com.constructora.backend.entity.ImagenProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenProyectoRepository extends JpaRepository<ImagenProyecto, Long> {
    
    List<ImagenProyecto> findByProyectoIdOrderByOrdenAsc(Long proyectoId);
    
    void deleteByProyectoId(Long proyectoId);
}
