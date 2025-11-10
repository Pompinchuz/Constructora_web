package com.constructora.backend.entity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_cliente", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(length = 20)
    private String telefono;
    
    private String direccion;
    
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;
    
    @OneToMany(mappedBy = "cliente")
    private List<SolicitudProforma> solicitudes;
    
    @OneToMany(mappedBy = "cliente")
    private List<Proforma> proformas;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
    
    // Método abstracto para obtener el nombre completo
    public abstract String getNombreCompleto();
}