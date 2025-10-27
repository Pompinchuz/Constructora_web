package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "clientesNaturales")
public class ClienteNatural {

    @Id
    @Column(name = "idCliente")
    private Short idCliente;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @Column(name = "dni", length = 8, nullable = false)
    private String dni;

    @Column(name = "nombres", length = 30, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 30, nullable = false)
    private String apellidos;
}
