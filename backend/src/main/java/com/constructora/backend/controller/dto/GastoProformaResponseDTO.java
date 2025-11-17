package com.constructora.backend.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class GastoProformaResponseDTO {
    private Long id;
    private String concepto;
    private String descripcion;
    private BigDecimal cantidad;
    private String unidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Integer orden;
}