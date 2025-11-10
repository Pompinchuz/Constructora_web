package com.constructora.backend.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.constructora.backend.entity.enums.EstadoProforma;
@Data
@Builder
public class ProformaResponseDTO {
    private Long id;
    private String codigo;
    private String clienteNombre;
    private String clienteCorreo;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private LocalDate vigenciaHasta;
    private String observaciones;
    private EstadoProforma estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
    private String creadoPor;
    private List<GastoProformaResponseDTO> gastos;
}