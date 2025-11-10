export enum EstadoComprobante {
    PENDIENTE = 'PENDIENTE',
    VERIFICADO = 'VERIFICADO',
    RECHAZADO = 'RECHAZADO'
  }
  
  export interface SubirComprobanteRequest {
    proformaId: number;
    monto: number;
    numeroOperacion?: string;
    entidadBancaria?: string;
    archivo: File;
  }
  
  export interface ComprobantePago {
    id: number;
    proformaId: number;
    codigoProforma: string;
    monto: number;
    numeroOperacion?: string;
    entidadBancaria?: string;
    archivoComprobante: string;
    estado: EstadoComprobante;
    observaciones?: string;
    fechaSubida: Date;
    verificadoPor?: string;
    fechaVerificacion?: Date;
  }