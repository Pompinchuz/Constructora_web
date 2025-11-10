package com.constructora.backend.controller.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}