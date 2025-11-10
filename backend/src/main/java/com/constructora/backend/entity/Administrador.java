package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(length = 50)
    private String cargo;
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}