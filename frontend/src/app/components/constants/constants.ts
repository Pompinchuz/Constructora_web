// ============================================
// CONSTANTES Y UTILIDADES
// ============================================

// constants.ts
export const ESTADOS_SOLICITUD_LABELS = {
    [EstadoSolicitud.PENDIENTE]: 'Pendiente',
    [EstadoSolicitud.EN_REVISION]: 'En revisión',
    [EstadoSolicitud.APROBADA]: 'Aprobada',
    [EstadoSolicitud.RECHAZADA]: 'Rechazada'
  };
  
  export const ESTADOS_PROFORMA_LABELS = {
    [EstadoProforma.ENVIADA]: 'Enviada',
    [EstadoProforma.VISTA]: 'Vista',
    [EstadoProforma.ACEPTADA]: 'Aceptada',
    [EstadoProforma.RECHAZADA]: 'Rechazada',
    [EstadoProforma.PAGADA]: 'Pagada'
  };
  
  export const ESTADOS_COMPROBANTE_LABELS = {
    [EstadoComprobante.PENDIENTE]: 'Pendiente',
    [EstadoComprobante.VERIFICADO]: 'Verificado',
    [EstadoComprobante.RECHAZADO]: 'Rechazado'
  };
  
  export const ESTADOS_SOLICITUD_COLORES = {
    [EstadoSolicitud.PENDIENTE]: 'warning',
    [EstadoSolicitud.EN_REVISION]: 'info',
    [EstadoSolicitud.APROBADA]: 'success',
    [EstadoSolicitud.RECHAZADA]: 'danger'
  };
  
  export const UNIDADES_MEDIDA = [
    'UND',
    'M2',
    'M3',
    'ML',
    'KG',
    'GLN',
    'PAQ',
    'CJA',
    'HR'
  ];
  