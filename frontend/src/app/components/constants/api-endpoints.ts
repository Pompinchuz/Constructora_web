// ============================================
// API-ENDPOINTS.TS - Endpoints de la API
// ============================================

export const API_ENDPOINTS = {
    // Auth
    AUTH: {
      LOGIN: '/auth/login',
      REGISTER_NATURAL: '/auth/registro/persona-natural',
      REGISTER_JURIDICO: '/auth/registro/persona-juridica',
      VALIDATE: '/auth/validate',
      CHECK_EMAIL: '/auth/check-email',
      CHECK_DNI: '/auth/check-dni',
      CHECK_RUC: '/auth/check-ruc'
    },
    
    // Solicitudes
    SOLICITUDES: {
      BASE: '/solicitudes',
      MIS_SOLICITUDES: '/solicitudes/mis-solicitudes',
      BY_ID: (id: number) => `/solicitudes/${id}`,
      ADMIN_TODAS: '/solicitudes/admin/todas',
      CAMBIAR_ESTADO: (id: number) => `/solicitudes/${id}/estado`,
      APROBAR: (id: number) => `/solicitudes/${id}/aprobar`,
      RECHAZAR: (id: number) => `/solicitudes/${id}/rechazar`,
      PENDIENTES_COUNT: '/solicitudes/admin/pendientes/count'
    },
    
    // Proformas
    PROFORMAS: {
      BASE: '/proformas',
      BY_ID: (id: number) => `/proformas/${id}`,
      BY_CODIGO: (codigo: string) => `/proformas/codigo/${codigo}`,
      MIS_PROFORMAS: '/proformas/mis-proformas',
      ADMIN_TODAS: '/proformas/admin/todas',
      ENVIAR: (id: number) => `/proformas/${id}/enviar`,
      CAMBIAR_ESTADO: (id: number) => `/proformas/${id}/estado`,
      MARCAR_VISTA: (id: number) => `/proformas/${id}/marcar-vista`,
      ESTADISTICAS: '/proformas/admin/estadisticas'
    },
    
    // Comprobantes
    COMPROBANTES: {
      BASE: '/comprobantes',
      BY_ID: (id: number) => `/comprobantes/${id}`,
      MIS_COMPROBANTES: '/comprobantes/mis-comprobantes',
      BY_PROFORMA: (id: number) => `/comprobantes/proforma/${id}`,
      ADMIN_TODOS: '/comprobantes/admin/todos',
      VERIFICAR: (id: number) => `/comprobantes/${id}/verificar`,
      RECHAZAR: (id: number) => `/comprobantes/${id}/rechazar`,
      CAMBIAR_ESTADO: (id: number) => `/comprobantes/${id}/estado`,
      PENDIENTES_COUNT: '/comprobantes/admin/pendientes/count'
    },
    
    // Contenido Web
    CONTENIDO: {
      // Imágenes - Admin
      IMAGENES: '/contenido/imagenes',
      IMAGEN_BY_ID: (id: number) => `/contenido/imagenes/${id}`,
      
      // Proyectos Exitosos
      PROYECTOS: '/contenido/proyectos',
      PROYECTOS_ACTIVOS: '/contenido/proyectos/activos',
      PROYECTO_BY_ID: (id: number) => `/contenido/proyectos/${id}`
    },
    
    // Archivos
    FILES: {
      DOWNLOAD: (folder: string, filename: string) => `/files/download/${folder}/${filename}`,
      VIEW: (folder: string, filename: string) => `/files/view/${folder}/${filename}`,
      PUBLIC_IMAGE: (filename: string) => `/files/public/imagenes/${filename}`
    }
  };