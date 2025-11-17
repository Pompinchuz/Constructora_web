package com.constructora.backend.controller.dto;

import com.constructora.backend.controller.dto.cliente.ClienteResponseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientePersonaJuridicaResponseDTO extends ClienteResponseDTO {
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
    
    @Override
    public String getNombreCompleto() {
        return razonSocial;
    }
}