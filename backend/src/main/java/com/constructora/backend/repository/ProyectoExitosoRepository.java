package com.constructora.backend.repository;

import com.constructora.backend.entity.ProyectoExitoso;
import com.constructora.backend.entity.enums.EstadoAprobacionProyecto;
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

    // Métodos para gestión de aprobaciones
    List<ProyectoExitoso> findByClienteIdAndEstadoAprobacionOrderByFechaCreacionDesc(
            Long clienteId, EstadoAprobacionProyecto estadoAprobacion);

    List<ProyectoExitoso> findByClienteIdOrderByFechaCreacionDesc(Long clienteId);

    List<ProyectoExitoso> findByEstadoAprobacionOrderByFechaCreacionDesc(
            EstadoAprobacionProyecto estadoAprobacion);
}