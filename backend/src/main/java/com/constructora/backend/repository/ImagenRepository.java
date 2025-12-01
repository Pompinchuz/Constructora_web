package com.constructora.backend.repository;

import com.constructora.backend.entity.Imagen;
import com.constructora.backend.entity.enums.TipoImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    
    List<Imagen> findByTipoAndActivoTrueOrderByOrdenAsc(TipoImagen tipo);
    
    List<Imagen> findByTipoOrderByOrdenAsc(TipoImagen tipo);
    
    List<Imagen> findByActivoTrueOrderByOrdenAsc();
    
    long countByTipo(TipoImagen tipo);
}
