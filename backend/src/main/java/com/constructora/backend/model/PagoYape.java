package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "pagoYape")
public class PagoYape {

    @Id
    @Column(name = "idPago")
    private Short idPago;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPago")
    private Pago pago;

    @Column(name = "numeroTelefono", length = 9, nullable = false)
    private String numeroTelefono;

    @Column(name = "nombreTitular", length = 100, nullable = false)
    private String nombreTitular;

    @Column(name = "idTransaccion", length = 50, nullable = false)
    private String idTransaccion;
}
