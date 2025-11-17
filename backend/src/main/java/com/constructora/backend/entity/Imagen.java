package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.TipoImagen;




@Entity
@Table(name = "imagenes")
@Data
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImagen tipo;
    
    @Column(length = 150)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false, length = 500)
    private String urlImagen;
    
    private Integer orden = 0;
    private Boolean activo = true;
    
    @Column(updatable = false)
    private LocalDateTime fechaSubida;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }
}