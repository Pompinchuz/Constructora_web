// ============================================
// MODELOS DE USUARIO Y AUTENTICACIÓN
// ============================================

// auth.models.ts
// src/app/components/models/auth.models.ts
export enum TipoUsuario {
    CLIENTE_NATURAL = 'CLIENTE_NATURAL',
    CLIENTE_JURIDICO = 'CLIENTE_JURIDICO',
    ADMINISTRADOR = 'ADMINISTRADOR'
  }
  
  export interface LoginRequest {
    correoElectronico: string;
    contrasena: string;
  }
  
  export interface LoginResponse {
    token: string;
    tipoToken: string;
    expiraEn: number;
    correoElectronico: string;
    tipoUsuario: TipoUsuario;
    nombreCompleto: string;
  }
  
  export interface RegistroPersonaNatural {
    correoElectronico: string;
    contrasena: string;
    nombres: string;
    apellidos: string;
    dni?: string;
    telefono?: string;
    direccion?: string;
    fechaNacimiento?: string;
  }
  
  export interface RegistroPersonaJuridica {
    correoElectronico: string;
    contrasena: string;
    razonSocial: string;
    ruc: string;
    representanteLegal?: string;
    telefono?: string;
    direccion?: string;
  }
  