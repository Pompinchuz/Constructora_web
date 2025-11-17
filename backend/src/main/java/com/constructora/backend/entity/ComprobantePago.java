package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.constructora.backend.entity.enums.EstadoComprobante;
@Entity
@Table(name = "comprobantes_pago")
@Data
@NoArgsConstructor
public class ComprobantePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "proformaId", nullable = false)
    private Proforma proforma;
    
    @ManyToOne
    @JoinColumn(name = "clienteId", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;
    
    @Column(length = 50)
    private String numeroOperacion;
    
    @Column(length = 100)
    private String entidadBancaria;
    
    @Column(nullable = false, length = 500)
    private String archivoComprobante;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoComprobante estado = EstadoComprobante.PENDIENTE;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(updatable = false)
    private LocalDateTime fechaSubida;
    
    @ManyToOne
    @JoinColumn(name = "verificadoPor")
    private Administrador verificadoPor;
    
    private LocalDateTime fechaVerificacion;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }
}