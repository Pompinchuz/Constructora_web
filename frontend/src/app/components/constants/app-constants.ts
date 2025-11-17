// ============================================
// APP-CONSTANTS.TS - COMPLETO
// ============================================

export const APP_CONSTANTS = {
  // Storage keys
  STORAGE_KEYS: {
    TOKEN: 'auth_token',
    USER: 'current_user',
    USER_TYPE: 'user_type'
  },
  
  // Roles (SIN el prefijo ROLE_ porque el backend no lo usa)
  ROLES: {
    ADMIN: 'ADMINISTRADOR',
    CLIENTE_NATURAL: 'CLIENTE_NATURAL',
    CLIENTE_JURIDICO: 'CLIENTE_JURIDICO'
  },
  
  // Validaciones
  VALIDATION: {
    DNI_LENGTH: 8,
    RUC_LENGTH: 11,
    PHONE_LENGTH: 9,
    PASSWORD_MIN_LENGTH: 6,
    MAX_FILE_SIZE: 10 * 1024 * 1024, // 10MB
    ALLOWED_IMAGE_TYPES: ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'],
    ALLOWED_DOCUMENT_TYPES: ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png']
  },
  
  // Paginación
  PAGINATION: {
    DEFAULT_PAGE_SIZE: 10,
    PAGE_SIZE_OPTIONS: [5, 10, 25, 50, 100]
  },
  
  // Fechas
  DATE_FORMATS: {
    DISPLAY: 'dd/MM/yyyy',
    API: 'yyyy-MM-dd',
    DATETIME: 'dd/MM/yyyy HH:mm'
  },
  
  // Mensajes
  MESSAGES: {
    SUCCESS: {
      LOGIN: 'Inicio de sesión exitoso',
      LOGOUT: 'Sesión cerrada exitosamente',
      REGISTER: 'Registro exitoso',
      CREATE: 'Creado exitosamente',
      UPDATE: 'Actualizado exitosamente',
      DELETE: 'Eliminado exitosamente',
      SAVE: 'Guardado exitosamente'
    },
    ERROR: {
      GENERIC: 'Ocurrió un error. Por favor, intenta nuevamente',
      NETWORK: 'Error de conexión. Verifica tu internet',
      UNAUTHORIZED: 'No tienes permisos para esta acción',
      SESSION_EXPIRED: 'Tu sesión ha expirado. Por favor, inicia sesión nuevamente',
      LOGIN: 'Error al iniciar sesión',
      REGISTER: 'Error al registrarse'
    }
  }
};