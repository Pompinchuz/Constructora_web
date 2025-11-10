package com.constructora.backend.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

import com.constructora.backend.controller.dto.cliente.ClienteResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientePersonaNaturalResponseDTO extends ClienteResponseDTO {
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    
    @Override
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}