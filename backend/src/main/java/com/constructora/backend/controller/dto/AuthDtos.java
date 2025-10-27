package com.constructora.backend.controller.dto;

public class AuthDtos {
    
public record LoginRequest(String correo, String password) {}
public record RegisterRequest(String nombre, String correo, String password) {}
public record AuthResponse(String token, String rol, String correo, String nombre) {}

}
