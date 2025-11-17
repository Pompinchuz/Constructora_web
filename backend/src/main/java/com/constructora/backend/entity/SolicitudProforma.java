package com.constructora.backend.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.EstadoSolicitud;



@Entity
@Table(name = "solicitudes_proforma")
@Data
@NoArgsConstructor
public class SolicitudProforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(length = 500)
    private String archivoAdjunto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
    
    @Column(columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(updatable = false)
    private LocalDateTime fechaSolicitud;
    
    private LocalDateTime fechaRevision;
    
    @ManyToOne
    @JoinColumn(name = "revisadoPor")
    private Administrador revisadoPor;
    
    @OneToOne(mappedBy = "solicitud")
    private Proforma proforma;
    
    @PrePersist
    protected void onCreate() {
        fechaSolicitud = LocalDateTime.now();
    }
}