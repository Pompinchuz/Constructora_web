// src/app/components/constants/estados.constants.ts
export const ESTADOS_SOLICITUD = {
    PENDIENTE: 'PENDIENTE',
    EN_REVISION: 'EN_REVISION',
    APROBADA: 'APROBADA',
    RECHAZADA: 'RECHAZADA'
  };
  
  export const ESTADOS_SOLICITUD_LABELS = {
    [ESTADOS_SOLICITUD.PENDIENTE]: 'Pendiente',
    [ESTADOS_SOLICITUD.EN_REVISION]: 'En Revisión',
    [ESTADOS_SOLICITUD.APROBADA]: 'Aprobada',
    [ESTADOS_SOLICITUD.RECHAZADA]: 'Rechazada'
  };
  
  export const ESTADOS_SOLICITUD_COLORS = {
    [ESTADOS_SOLICITUD.PENDIENTE]: 'warning',
    [ESTADOS_SOLICITUD.EN_REVISION]: 'info',
    [ESTADOS_SOLICITUD.APROBADA]: 'success',
    [ESTADOS_SOLICITUD.RECHAZADA]: 'danger'
  };
  
  export const ESTADOS_PROFORMA = {
    ENVIADA: 'ENVIADA',
    VISTA: 'VISTA',
    ACEPTADA: 'ACEPTADA',
    RECHAZADA: 'RECHAZADA',
    PAGADA: 'PAGADA'
  };
  
  export const ESTADOS_PROFORMA_LABELS = {
    [ESTADOS_PROFORMA.ENVIADA]: 'Enviada',
    [ESTADOS_PROFORMA.VISTA]: 'Vista',
    [ESTADOS_PROFORMA.ACEPTADA]: 'Aceptada',
    [ESTADOS_PROFORMA.RECHAZADA]: 'Rechazada',
    [ESTADOS_PROFORMA.PAGADA]: 'Pagada'
  };
  
  export const ESTADOS_PROFORMA_COLORS = {
    [ESTADOS_PROFORMA.ENVIADA]: 'primary',
    [ESTADOS_PROFORMA.VISTA]: 'info',
    [ESTADOS_PROFORMA.ACEPTADA]: 'success',
    [ESTADOS_PROFORMA.RECHAZADA]: 'danger',
    [ESTADOS_PROFORMA.PAGADA]: 'success'
  };
  
  export const ESTADOS_COMPROBANTE = {
    PENDIENTE: 'PENDIENTE',
    VERIFICADO: 'VERIFICADO',
    RECHAZADO: 'RECHAZADO'
  };
  
  export const ESTADOS_COMPROBANTE_LABELS = {
    [ESTADOS_COMPROBANTE.PENDIENTE]: 'Pendiente',
    [ESTADOS_COMPROBANTE.VERIFICADO]: 'Verificado',
    [ESTADOS_COMPROBANTE.RECHAZADO]: 'Rechazado'
  };
  
  export const ESTADOS_COMPROBANTE_COLORS = {
    [ESTADOS_COMPROBANTE.PENDIENTE]: 'warning',
    [ESTADOS_COMPROBANTE.VERIFICADO]: 'success',
    [ESTADOS_COMPROBANTE.RECHAZADO]: 'danger'
  };
  
  export const TIPO_IMAGEN = {
    PORTADA: 'PORTADA',
    SERVICIO: 'SERVICIO',
    GALERIA: 'GALERIA',
    SOBRE_NOSOTROS: 'SOBRE_NOSOTROS'
  };
  
  export const TIPO_IMAGEN_LABELS = {
    [TIPO_IMAGEN.PORTADA]: 'Portada',
    [TIPO_IMAGEN.SERVICIO]: 'Servicio',
    [TIPO_IMAGEN.GALERIA]: 'Galería',
    [TIPO_IMAGEN.SOBRE_NOSOTROS]: 'Sobre Nosotros'
  };
  
  export const UNIDADES_MEDIDA = [
    { value: 'UND', label: 'Unidad' },
    { value: 'M2', label: 'Metro Cuadrado' },
    { value: 'M3', label: 'Metro Cúbico' },
    { value: 'ML', label: 'Metro Lineal' },
    { value: 'KG', label: 'Kilogramo' },
    { value: 'GLN', label: 'Galón' },
    { value: 'PAQ', label: 'Paquete' },
    { value: 'CJA', label: 'Caja' },
    { value: 'HR', label: 'Hora' }
  ];