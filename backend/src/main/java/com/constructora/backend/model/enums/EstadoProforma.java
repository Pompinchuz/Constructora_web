package com.constructora.backend.model.enums;

public enum EstadoProforma {
    EN_PROCESO,
    FINALIZADO,
    CANCELADO
}































     /*
      * 
        @author: huillaRosillo

        Nota: Para EstadoProforma cambié el espacio por _ en el enum. Se mapea como STRING, así que los valores en DB serán 
        “en_proceso/finalizado/cancelado”. Si se quiere que quede exactamente “en proceso”, se podria cambiar el enum por un @Converter o 
        usar un @JsonProperty y @Enumerated(STRING) + @Column(columnDefinition="ENUM('en proceso','finalizado','cancelado')"). 
        Para mantener 1:1 con el DDL, también se podria usar strings simples en el servicio.




      */