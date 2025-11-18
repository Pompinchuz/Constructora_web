package com.constructora.backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ComprobantePagoRequestDTO {

    @NotNull(message = "El ID de la proforma es requerido")
    private Long proformaId;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    private String numeroOperacion;

    private String entidadBancaria;

    private String observaciones;

    @NotNull(message = "El archivo del comprobante es requerido")
    private MultipartFile archivo;
}
