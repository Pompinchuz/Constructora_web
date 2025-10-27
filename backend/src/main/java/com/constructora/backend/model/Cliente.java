package com.constructora.backend.model;


import com.constructora.backend.model.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "clientes", indexes = {
    @Index(name = "ix_clientes_correo", columnList = "correo", unique = true)
})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Short idCliente;

    @Column(name = "correo", length = 50, nullable = false, unique = true)
    private String correo;

    @Column(name = "celular", length = 9, nullable = false)
    private String celular;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoCliente", nullable = false, length = 10)
    private TipoCliente tipoCliente;

    // Conveniencia (opcionales)
    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private List<FormularioContacto> formularios = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private List<Proyecto> proyectos = new ArrayList<>();
}
