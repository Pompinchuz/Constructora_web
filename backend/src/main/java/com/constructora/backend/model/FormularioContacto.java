package com.constructora.backend.model;

import com.constructora.backend.model.enums.EstadoProforma;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "formulariosContacto", indexes = {
    @Index(name = "ix_form_contacto_idCliente", columnList = "idCliente"),
    @Index(name = "ix_form_contacto_idPago", columnList = "idPago")
})
public class FormularioContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFormulario")
    private Short idFormulario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCliente", nullable = false, foreignKey = @ForeignKey(name = "formulariosContacto_ibfk_1"))
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idPago", foreignKey = @ForeignKey(name = "formulariosContacto_ibfk_2"))
    private Pago pago; // opcional

    @Column(name = "mensaje", columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoProforma", nullable = false, length = 15)
    private EstadoProforma estadoProforma;

    // DEFAULT CURRENT_TIMESTAMP en BD
    @Column(name = "fechaEnvio", nullable = false, insertable = false, updatable = false)
    private Instant fechaEnvio;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DocumentoAdjunto> documentos = new ArrayList<>();
}
