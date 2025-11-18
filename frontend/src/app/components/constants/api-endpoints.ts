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
      BASE: '/api/solicitudes',
      MIS_SOLICITUDES: '/api/solicitudes/mis-solicitudes',
      BY_ID: (id: number) => `/api/solicitudes/${id}`,
      ADMIN_TODAS: '/api/solicitudes/admin/todas',
      CAMBIAR_ESTADO: (id: number) => `/api/solicitudes/${id}/estado`,
      APROBAR: (id: number) => `/api/solicitudes/${id}/aprobar`,
      RECHAZAR: (id: number) => `/api/solicitudes/${id}/rechazar`,
      PENDIENTES_COUNT: '/api/solicitudes/admin/pendientes/count'
    },
    
    // Proformas
    PROFORMAS: {
      BASE: '/api/proformas',
      BY_ID: (id: number) => `/api/proformas/${id}`,
      BY_CODIGO: (codigo: string) => `/api/proformas/codigo/${codigo}`,
      MIS_PROFORMAS: '/api/proformas/mis-proformas',
      ADMIN_TODAS: '/api/proformas/admin/todas',
      ENVIAR: (id: number) => `/api/proformas/${id}/enviar`,
      CAMBIAR_ESTADO: (id: number) => `/api/proformas/${id}/estado`,
      MARCAR_VISTA: (id: number) => `/api/proformas/${id}/marcar-vista`,
      ESTADISTICAS: '/api/proformas/admin/estadisticas'
    },
    
    // Comprobantes
    COMPROBANTES: {
      BASE: '/api/comprobantes',
      BY_ID: (id: number) => `/api/comprobantes/${id}`,
      MIS_COMPROBANTES: '/api/comprobantes/mis-comprobantes',
      BY_PROFORMA: (id: number) => `/api/comprobantes/proforma/${id}`,
      ADMIN_TODOS: '/api/comprobantes/admin/todos',
      VERIFICAR: (id: number) => `/api/comprobantes/${id}/verificar`,
      RECHAZAR: (id: number) => `/api/comprobantes/${id}/rechazar`,
      CAMBIAR_ESTADO: (id: number) => `/api/comprobantes/${id}/estado`,
      PENDIENTES_COUNT: '/api/comprobantes/admin/pendientes/count'
    },
    
    // Contenido Web
    CONTENIDO: {
      // Imágenes - Admin
      IMAGENES: '/api/contenido/imagenes',
      IMAGEN_BY_ID: (id: number) => `/api/contenido/imagenes/${id}`,

      // Proyectos Exitosos
      PROYECTOS: '/api/contenido/proyectos',
      PROYECTOS_ACTIVOS: '/api/contenido/proyectos/activos',
      PROYECTO_BY_ID: (id: number) => `/api/contenido/proyectos/${id}`
    },
    
    // Archivos
    FILES: {
      DOWNLOAD: (folder: string, filename: string) => `/api/files/download/${folder}/${filename}`,
      VIEW: (folder: string, filename: string) => `/api/files/view/${folder}/${filename}`,
      PUBLIC_IMAGE: (filename: string) => `/api/files/public/imagenes/${filename}`
    }
  };