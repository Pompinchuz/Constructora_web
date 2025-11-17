package com.constructora.backend.repository;

import com.constructora.backend.entity.ComprobantePago;
import com.constructora.backend.entity.enums.EstadoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    
    List<ComprobantePago> findByProformaIdOrderByFechaSubidaDesc(Long proformaId);
    
    List<ComprobantePago> findByClienteIdOrderByFechaSubidaDesc(Long clienteId);
    
    List<ComprobantePago> findByEstadoOrderByFechaSubidaDesc(EstadoComprobante estado);
    
    @Query("SELECT c FROM ComprobantePago c WHERE c.estado = 'PENDIENTE' ORDER BY c.fechaSubida ASC")
    List<ComprobantePago> findComprobantesPendientes();
    
    long countByEstado(EstadoComprobante estado);
    
    boolean existsByProformaIdAndEstado(Long proformaId, EstadoComprobante estado);
}
