// ============================================
// MODELOS DE SOLICITUD Y PROFORMA
// ============================================

// solicitud.models.ts
// src/app/components/models/solicitud.models.ts
export enum EstadoSolicitud {
    PENDIENTE = 'PENDIENTE',
    EN_REVISION = 'EN_REVISION',
    APROBADA = 'APROBADA',
    RECHAZADA = 'RECHAZADA'
  }
  
  export interface SolicitudProformaRequest {
    titulo: string;
    descripcion: string;
    archivo?: File;
  }
  
  export interface SolicitudProforma {
    id: number;
    titulo: string;
    descripcion: string;
    archivoAdjunto?: string;
    estado: EstadoSolicitud;
    motivoRechazo?: string;
    fechaSolicitud: Date;
    fechaRevision?: Date;
    revisadoPor?: string;
    clienteNombre: string;
  }