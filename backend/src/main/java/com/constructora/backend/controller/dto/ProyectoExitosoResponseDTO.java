package com.constructora.backend.controller.dto;

import com.constructora.backend.entity.enums.EstadoAprobacionProyecto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProyectoExitosoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    private String imagenPrincipal;
    private List<String> imagenes;
    private Boolean activo;

    // Campos de aprobación
    private Long clienteId;
    private String clienteNombre;  // Nombre completo del cliente
    private EstadoAprobacionProyecto estadoAprobacion;
    private String motivoRechazo;
    private LocalDateTime fechaSolicitudAprobacion;
    private LocalDateTime fechaRespuestaCliente;
}