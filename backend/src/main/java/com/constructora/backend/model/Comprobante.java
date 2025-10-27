package com.constructora.backend.model;

import com.constructora.backend.model.enums.TipoComprobante;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "comprobantes", indexes = {
    @Index(name = "ix_comprobantes_idPago", columnList = "idPago")
})
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComprobante")
    private Short idComprobante;

    @ManyToOne
    @JoinColumn(name = "idPago", foreignKey = @ForeignKey(name = "comprobantes_ibfk_1"))
    private Pago pago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoComprobante", nullable = false, length = 10)
    private TipoComprobante tipoComprobante;

    @Column(name = "numeroSerie", length = 4, nullable = false)
    private String numeroSerie;

    @Column(name = "numeroCorrelativo", nullable = false)
    private Short numeroCorrelativo;

    // DEFAULT CURRENT_TIMESTAMP en BD
    @Column(name = "fechaEmision", nullable = false, insertable = false, updatable = false)
    private Instant fechaEmision;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
}
