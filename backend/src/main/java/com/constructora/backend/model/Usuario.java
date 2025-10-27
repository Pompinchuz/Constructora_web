package com.constructora.backend.model;

import com.constructora.backend.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "ix_usuarios_correo", columnList = "correo", unique = true)
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Short idUsuario;

    @Column(name = "nombreUsuario", length = 50, nullable = false)
    private String nombreUsuario;

    @Column(name = "correo", length = 50, nullable = false, unique = true)
    private String correo;

   
    @Column(name = "contrasenaHash", length = 255, nullable = false)
    private String contrasenaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 10)
    private Rol rol;
}
