package com.constructora.backend.repository;

import com.constructora.backend.entity.SolicitudProforma;
import com.constructora.backend.entity.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudProformaRepository extends JpaRepository<SolicitudProforma, Long> {
    
    List<SolicitudProforma> findByClienteIdOrderByFechaSolicitudDesc(Long clienteId);
    
    List<SolicitudProforma> findByEstadoOrderByFechaSolicitudDesc(EstadoSolicitud estado);
    
    List<SolicitudProforma> findAllByOrderByFechaSolicitudDesc();
    
    @Query("SELECT s FROM SolicitudProforma s WHERE s.estado = :estado AND s.fechaSolicitud >= :fechaDesde")
    List<SolicitudProforma> findByEstadoAndFechaSolicitudGreaterThanEqual(
            @Param("estado") EstadoSolicitud estado,
            @Param("fechaDesde") LocalDateTime fechaDesde
    );
    
    long countByEstado(EstadoSolicitud estado);
    
    long countByClienteId(Long clienteId);
    
    @Query("SELECT COUNT(s) FROM SolicitudProforma s WHERE s.estado = 'PENDIENTE' OR s.estado = 'EN_REVISION'")
    long countSolicitudesPendientes();
}
