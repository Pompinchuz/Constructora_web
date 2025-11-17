package com.constructora.backend.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "proyectos_exitosos")
@Data
@NoArgsConstructor
public class ProyectoExitoso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(length = 200)
    private String ubicacion;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    
    @Column(length = 500)
    private String imagenPrincipal;
    
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<ImagenProyecto> imagenes;
    
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}