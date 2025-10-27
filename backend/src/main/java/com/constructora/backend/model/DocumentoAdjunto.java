package com.constructora.backend.model;

import com.constructora.backend.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "documentosAdjuntos", indexes = {
    @Index(name = "ix_docs_idFormulario", columnList = "idFormulario")
})
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocumento")
    private Short idDocumento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idFormulario", nullable = false, foreignKey = @ForeignKey(name = "documentosAdjuntos_ibfk_1"))
    private FormularioContacto formulario;

    @Column(name = "nombreArchivo", length = 50, nullable = false)
    private String nombreArchivo;

    @Column(name = "rutaArchivo", length = 120, nullable = false)
    private String rutaArchivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoDocumento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

  
    @Column(name = "fechaSubida", nullable = false, insertable = false, updatable = false)
    private Instant fechaSubida;
}
