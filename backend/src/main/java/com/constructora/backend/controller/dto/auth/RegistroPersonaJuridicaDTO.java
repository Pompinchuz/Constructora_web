package com.constructora.backend.controller.dto.auth;

import com.constructora.backend.controller.dto.RegistroClienteDTO;
import com.constructora.backend.entity.enums.TipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroPersonaJuridicaDTO extends RegistroClienteDTO {
    
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no debe exceder 200 caracteres")
    private String razonSocial;
    
    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener 11 dígitos")
    private String ruc;
    
    @Size(max = 150, message = "El nombre del representante no debe exceder 150 caracteres")
    private String representanteLegal;
    
    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.CLIENTE_JURIDICO;
    }
}