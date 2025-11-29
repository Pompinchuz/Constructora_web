package com.constructora.backend.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
@Data
public class ProyectoExitosoDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String nombre;

    private String descripcion;

    @Size(max = 200)
    private String ubicacion;

    @Past(message = "La fecha de inicio debe ser pasada")
    private LocalDate fechaInicio;

    @Past(message = "La fecha de finalización debe ser pasada")
    private LocalDate fechaFinalizacion;

    private MultipartFile imagenPrincipal;
    private List<MultipartFile> imagenesAdicionales;

    // ID del cliente al que pertenece el proyecto
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
}