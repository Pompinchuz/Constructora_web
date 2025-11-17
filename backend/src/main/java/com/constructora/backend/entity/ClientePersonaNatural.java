package com.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;



@Entity
@Table(name = "clientes_persona_natural")
@PrimaryKeyJoinColumn(name = "clienteId")
@DiscriminatorValue("NATURAL")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientePersonaNatural extends Cliente {
    
    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(unique = true, length = 8)
    private String dni;
    
    private LocalDate fechaNacimiento;
    
    @Override
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}