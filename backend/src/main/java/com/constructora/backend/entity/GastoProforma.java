package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "gastos_proforma")
@Data
@NoArgsConstructor
public class GastoProforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proformaId", nullable = false)
    private Proforma proforma;
    
    @Column(nullable = false, length = 200)
    private String concepto;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad = BigDecimal.ONE;
    
    @Column(length = 20)
    private String unidad = "UND";
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    private Integer orden = 0;
}