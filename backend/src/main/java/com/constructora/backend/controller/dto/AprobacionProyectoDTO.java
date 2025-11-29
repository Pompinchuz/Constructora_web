package com.constructora.backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AprobacionProyectoDTO {

    @NotNull(message = "El ID del proyecto es obligatorio")
    private Long proyectoId;

    @NotNull(message = "La decisión de aprobación es obligatoria")
    private Boolean aprobado;  // true = aprobar, false = rechazar

    @Size(max = 500, message = "El motivo no puede exceder los 500 caracteres")
    private String motivoRechazo;  // Requerido si aprobado = false
}
