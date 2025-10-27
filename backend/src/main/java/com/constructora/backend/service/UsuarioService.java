// src/main/java/com/constructora/backend/service/UsuarioService.java
package com.constructora.backend.service;

import com.constructora.backend.model.Usuario;
import com.constructora.backend.model.enums.Rol;

public interface UsuarioService {
    Usuario registrarCliente(String nombre, String correo, String contrasena);
    Usuario buscarPorCorreo(String correo);
    boolean existeAdmin();
    Usuario crearAdminSiNoExiste(String nombre, String correo, String contrasena);
}
