// ============================================
// MODELOS DE CLIENTE
// ============================================

// cliente.models.ts
// src/app/components/models/cliente.models.ts
export interface Cliente {
    id: number;
    correoElectronico: string;
    telefono?: string;
    direccion?: string;
    fechaRegistro: Date;
    activo: boolean;
    tipoCliente: 'NATURAL' | 'JURIDICO';
  }
  
  export interface ClientePersonaNatural extends Cliente {
    nombres: string;
    apellidos: string;
    dni?: string;
    fechaNacimiento?: Date;
  }
  
  export interface ClientePersonaJuridica extends Cliente {
    razonSocial: string;
    ruc: string;
    representanteLegal?: string;
  }