package com.constructora.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class SolicitudProformaDTO {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no debe exceder 200 caracteres")
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    private MultipartFile archivo;
}