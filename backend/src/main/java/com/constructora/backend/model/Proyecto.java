package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "proyectos", indexes = {
    @Index(name = "ix_proyectos_idCliente", columnList = "idCliente")
})
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProyecto")
    private Short idProyecto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCliente", nullable = false, foreignKey = @ForeignKey(name = "proyectos_ibfk_1"))
    private Cliente cliente;

    @Column(name = "nombreCliente", length = 150, nullable = false)
    private String nombreCliente;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "duracion", nullable = false)
    private Short duracion;

    // DEFAULT CURRENT_TIMESTAMP en BD
    @Column(name = "fechaPublicacion", nullable = false, insertable = false, updatable = false)
    private Instant fechaPublicacion;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ImagenProyecto> imagenes = new ArrayList<>();
}
