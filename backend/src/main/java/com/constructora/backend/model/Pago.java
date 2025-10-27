package com.constructora.backend.model;

import com.constructora.backend.model.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "pagos", indexes = {
    @Index(name = "ix_pagos_idCliente", columnList = "idCliente")
})
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPago")
    private Short idPago;

    @ManyToOne
    @JoinColumn(name = "idCliente", foreignKey = @ForeignKey(name = "pagos_ibfk_1"))
    private Cliente cliente;

    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoPago", nullable = false, length = 10)
    private EstadoPago estadoPago;

    // DEFAULT CURRENT_TIMESTAMP en BD
    @Column(name = "fechaPago", nullable = false, insertable = false, updatable = false)
    private Instant fechaPago;

    // Relaciones 1:1 opcionales (conveniencia)
    @OneToOne(mappedBy = "pago")
    private PagoCredito pagoCredito;

    @OneToOne(mappedBy = "pago")
    private PagoDebito pagoDebito;

    @OneToOne(mappedBy = "pago")
    private PagoYape pagoYape;

    @OneToOne(mappedBy = "pago")
    private PagoPlin pagoPlin;
}
