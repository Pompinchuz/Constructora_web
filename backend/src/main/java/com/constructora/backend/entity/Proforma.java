package com.constructora.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.constructora.backend.entity.enums.EstadoProforma;
@Entity
@Table(name = "proformas")
@Data
@NoArgsConstructor
public class Proforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "solicitudId", nullable = false, unique = true)
    private SolicitudProforma solicitud;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @ManyToOne
    @JoinColumn(name = "clienteId", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal igv = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private LocalDate vigenciaHasta;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProforma estado = EstadoProforma.ENVIADA;
    
    @ManyToOne
    @JoinColumn(name = "creadoPor", nullable = false)
    private Administrador creadoPor;
    
    @OneToMany(mappedBy = "proforma", cascade = CascadeType.ALL)
    private List<GastoProforma> gastos;
    
    @OneToMany(mappedBy = "proforma")
    private List<ComprobantePago> comprobantes;
    
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaEnvio;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}