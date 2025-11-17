package com.constructora.backend.controller.dto.cliente;

import com.constructora.backend.controller.dto.ClientePersonaJuridicaResponseDTO;
import com.constructora.backend.controller.dto.ClientePersonaNaturalResponseDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipoCliente")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClientePersonaNaturalResponseDTO.class, name = "NATURAL"),
    @JsonSubTypes.Type(value = ClientePersonaJuridicaResponseDTO.class, name = "JURIDICO")
})
public abstract class ClienteResponseDTO {
    private Long id;
    private String correoElectronico;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
    
    public abstract String getNombreCompleto();
}