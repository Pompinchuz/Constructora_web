package com.constructora.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "imagenesProyecto", indexes = {
    @Index(name = "ix_img_proyecto_idProyecto", columnList = "idProyecto")
})
public class ImagenProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idImagen")
    private Short idImagen;

    @ManyToOne
    @JoinColumn(name = "idProyecto", foreignKey = @ForeignKey(name = "imagenesProyecto_ibfk_1"))
    private Proyecto proyecto;

    @Column(name = "rutaImagen", length = 255, nullable = false)
    private String rutaImagen;

    @Column(name = "descripcionImagen", columnDefinition = "TEXT", nullable = false)
    private String descripcionImagen;

    // DEFAULT CURRENT_TIMESTAMP en BD
    @Column(name = "fechaSubida", nullable = false, insertable = false, updatable = false)
    private Instant fechaSubida;
}
