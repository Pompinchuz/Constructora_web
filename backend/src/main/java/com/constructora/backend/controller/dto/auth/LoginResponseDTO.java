package com.constructora.backend.controller.dto.auth;

import com.constructora.backend.entity.enums.TipoUsuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private String tipoToken;
    private Long expiraEn;
    private String correoElectronico;
    private TipoUsuario tipoUsuario;
    private String nombreCompleto;
}