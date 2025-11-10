package com.constructora.backend.repository;

import com.constructora.backend.entity.Proforma;
import com.constructora.backend.entity.enums.EstadoProforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProformaRepository extends JpaRepository<Proforma, Long> {
    
    List<Proforma> findByClienteIdOrderByFechaCreacionDesc(Long clienteId);
    
    List<Proforma> findByEstadoOrderByFechaCreacionDesc(EstadoProforma estado);
    
    Optional<Proforma> findByCodigo(String codigo);
    
    Optional<Proforma> findBySolicitudId(Long solicitudId);
    
    @Query("SELECT COUNT(p) FROM Proforma p WHERE p.codigo LIKE CONCAT('PRF-', :anio, '-%')")
    Long contarProformasDelAnio(@Param("anio") String anio);
    
    @Query("SELECT p FROM Proforma p WHERE p.vigenciaHasta < :fecha AND p.estado = 'ENVIADA'")
    List<Proforma> findProformasVencidas(@Param("fecha") LocalDate fecha);
    
    long countByEstado(EstadoProforma estado);
    
    @Query("SELECT SUM(p.total) FROM Proforma p WHERE p.estado = 'PAGADA'")
    Double calcularTotalFacturado();
}