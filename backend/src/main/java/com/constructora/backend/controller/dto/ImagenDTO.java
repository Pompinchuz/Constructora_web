package com.constructora.backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import com.constructora.backend.entity.enums.TipoImagen;

@Data
public class ImagenDTO {
    private TipoImagen tipo;
    
    @Size(max = 150)
    private String titulo;
    
    private String descripcion;
    
    @NotNull(message = "La imagen es obligatoria")
    private MultipartFile archivo;
    
    private Integer orden;
}