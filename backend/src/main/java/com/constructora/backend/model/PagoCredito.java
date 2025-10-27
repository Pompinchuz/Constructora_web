package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "pagoCredito")
public class PagoCredito {

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

    @Column(name = "fechaExpiracion", length = 5, nullable = false)
    private String fechaExpiracion; // MM/AA

    @Column(name = "codigoCVV", length = 3, nullable = false)
    private String codigoCVV;
}
