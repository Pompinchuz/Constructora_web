package com.constructora.backend.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
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
}