package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "clientesEmpresas")
public class ClienteEmpresa {

    @Id
    @Column(name = "idCliente")
    private Short idCliente;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @Column(name = "ruc", length = 11, nullable = false)
    private String ruc;

    @Column(name = "razonSocial", length = 150, nullable = false)
    private String razonSocial;
}
