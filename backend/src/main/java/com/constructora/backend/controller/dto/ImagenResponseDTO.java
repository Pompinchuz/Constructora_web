package com.constructora.backend.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.TipoImagen;

@Data
@Builder
public class ImagenResponseDTO {
    private Long id;
    private TipoImagen tipo;
    private String titulo;
    private String descripcion;
    private String urlImagen;
    private Integer orden;
    private Boolean activo;
    private LocalDateTime fechaSubida;
}