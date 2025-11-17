package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagenes_proyectos")
@Data
@NoArgsConstructor
public class ImagenProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyectoId", nullable = false)
    private ProyectoExitoso proyecto;
    
    @Column(nullable = false, length = 500)
    private String urlImagen;
    
    private String descripcion;
    private Integer orden = 0;
}