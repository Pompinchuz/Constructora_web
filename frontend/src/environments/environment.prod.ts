// ============================================
// CONFIGURACIÓN DE PRODUCCIÓN - NETLIFY
// ============================================

export const environment = {
  production: true,

  // URL de tu backend en Railway
  // IMPORTANTE: Cambia esto por tu URL real de Railway después del despliegue
  // Ejemplo: https://constructora-backend-production.up.railway.app
  apiUrl: 'https://tu-backend.railway.app',

  // URL para uploads (mismo dominio del backend)
  uploadUrl: 'https://tu-backend.railway.app/uploads',

  // Configuración de la aplicación
  appName: 'Constructora Sistema de Proformas',
  version: '1.0.0',

  // Configuración de logs (desactivados en producción)
  enableDebugLogs: false,

  // Timeout para requests HTTP (en ms)
  httpTimeout: 30000
};
