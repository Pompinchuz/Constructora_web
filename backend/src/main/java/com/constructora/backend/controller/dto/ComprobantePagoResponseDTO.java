package com.constructora.backend.controller.dto;

import com.constructora.backend.entity.enums.EstadoComprobante;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ComprobantePagoResponseDTO {

    private Long id;
    private Long proformaId;
    private String proformaCodigo;
    private String clienteNombre;
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
