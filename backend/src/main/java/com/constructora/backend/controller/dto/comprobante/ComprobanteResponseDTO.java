package com.constructora.backend.controller.dto.comprobante;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.EstadoComprobante;

@Data
@Builder
public class ComprobanteResponseDTO {
    private Long id;
    private Long proformaId;
    private String codigoProforma;
    private BigDecimal monto;
    private String numeroOperacion;
    private String entidadBancaria;
    private String archivoComprobante;
    private EstadoComprobante estado;
    private String observaciones;
    private LocalDateTime fechaSubida;
    private String verificadoPor;
    private LocalDateTime fechaVerificacion;
}