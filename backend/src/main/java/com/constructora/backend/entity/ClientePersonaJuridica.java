package com.constructora.backend.entity;


import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "clientes_persona_juridica")
@PrimaryKeyJoinColumn(name = "clienteId")
@DiscriminatorValue("JURIDICO")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientePersonaJuridica extends Cliente {
    
    @Column(nullable = false, length = 200)
    private String razonSocial;
    
    @Column(nullable = false, unique = true, length = 11)
    private String ruc;
    
    @Column(length = 150)
    private String representanteLegal;
    
    @Override
    public String getNombreCompleto() {
        return razonSocial;
    }
}