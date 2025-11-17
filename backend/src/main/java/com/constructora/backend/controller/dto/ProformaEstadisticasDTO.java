package com.constructora.backend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProformaEstadisticasDTO {
    private Long totalProformas;
    private Long proformasEnviadas;
    private Long proformasPagadas;
    private Long proformasRechazadas;
    private BigDecimal montoTotal;
    private BigDecimal montoFacturado;
}