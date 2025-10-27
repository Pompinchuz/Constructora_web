package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "pagoDebito")
public class PagoDebito {

    @Id
    @Column(name = "idPago")
    private Short idPago;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPago")
    private Pago pago;

    @Column(name = "numeroTarjeta", length = 16, nullable = false)
    private String numeroTarjeta;

    @Column(name = "nombreTitular", length = 100, nullable = false)
    private String nombreTitular;

    @Column(name = "entidadBancaria", length = 50, nullable = false)
    private String entidadBancaria;
}
