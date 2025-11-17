// proforma.models.ts
// src/app/components/models/proforma.models.ts
export enum EstadoProforma {
    ENVIADA = 'ENVIADA',
    VISTA = 'VISTA',
    ACEPTADA = 'ACEPTADA',
    RECHAZADA = 'RECHAZADA',
    PAGADA = 'PAGADA'
  }
  
  export interface GastoProforma {
    id?: number;
    concepto: string;
    descripcion?: string;
    cantidad: number;
    unidad: string;
    precioUnitario: number;
    subtotal?: number;
    orden?: number;
  }
  
  export interface CrearProformaRequest {
    solicitudId: number;
    vigenciaHasta: string;
    observaciones?: string;
    gastos: GastoProforma[];
  }
  
  export interface Proforma {
    id: number;
    codigo: string;
    clienteNombre: string;
    clienteCorreo: string;
    subtotal: number;
    igv: number;
    total: number;
    vigenciaHasta: Date;
    observaciones?: string;
    estado: EstadoProforma;
    fechaCreacion: Date;
    fechaEnvio?: Date;
    creadoPor: string;
    gastos: GastoProforma[];
  }