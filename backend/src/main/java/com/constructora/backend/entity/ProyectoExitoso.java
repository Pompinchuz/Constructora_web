package com.constructora.backend.entity;



import com.constructora.backend.entity.enums.EstadoAprobacionProyecto;
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

    private Boolean activo = false;  // Por defecto inactivo hasta que el cliente apruebe

    // ============================================
    // CAMPOS DE APROBACIÓN DEL CLIENTE
    // ============================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId")
    private Cliente cliente;  // Cliente asociado al proyecto

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAprobacionProyecto estadoAprobacion = EstadoAprobacionProyecto.PENDIENTE_APROBACION;

    @Column(columnDefinition = "TEXT")
    private String motivoRechazo;  // Razón por la cual el cliente rechazó la publicación

    private LocalDateTime fechaSolicitudAprobacion;  // Cuándo el admin solicitó la aprobación

    private LocalDateTime fechaRespuestaCliente;  // Cuándo el cliente respondió

    // ============================================

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<ImagenProyecto> imagenes;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}