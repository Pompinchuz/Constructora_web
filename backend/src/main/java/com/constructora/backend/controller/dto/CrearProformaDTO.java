package com.constructora.backend.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class CrearProformaDTO {
    @NotNull(message = "El ID de la solicitud es obligatorio")
    private Long solicitudId;
    
    @NotNull(message = "La fecha de vigencia es obligatoria")
    @Future(message = "La fecha de vigencia debe ser futura")
    private LocalDate vigenciaHasta;
    
    private String observaciones;
    
    @NotEmpty(message = "Debe incluir al menos un gasto")
    @Valid
    private List<GastoProformaDTO> gastos;
}