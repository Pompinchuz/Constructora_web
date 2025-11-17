package com.constructora.backend.controller.dto.comprobante;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class SubirComprobanteDTO {
    @NotNull(message = "El ID de la proforma es obligatorio")
    private Long proformaId;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;
    
    private String numeroOperacion;
    private String entidadBancaria;
    
    @NotNull(message = "El archivo del comprobante es obligatorio")
    private MultipartFile archivo;
}