package com.constructora.backend.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.EstadoSolicitud;
@Data
@Builder
public class SolicitudProformaResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String archivoAdjunto;
    private EstadoSolicitud estado;
    private String motivoRechazo;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRevision;
    private String revisadoPor;
    private String clienteNombre;
}