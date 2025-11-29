// ============================================
// MODELOS DE CONTENIDO WEB - ACTUALIZADOS
// ============================================

// src/app/components/models/contenido.models.ts

export enum TipoImagen {
  PORTADA = 'PORTADA',
  SERVICIO = 'SERVICIO',
  GALERIA = 'GALERIA',
  SOBRE_NOSOTROS = 'SOBRE_NOSOTROS'
}

export enum EstadoAprobacionProyecto {
  PENDIENTE_APROBACION = 'PENDIENTE_APROBACION',
  APROBADO = 'APROBADO',
  RECHAZADO = 'RECHAZADO'
}

export interface Imagen {
  id: number;
  tipo: TipoImagen;
  titulo?: string;
  descripcion?: string;
  urlImagen: string;
  orden: number;
  activo: boolean;
  fechaSubida: string; // ISO string from backend
}

export interface ProyectoExitoso {
  id: number;
  nombre: string;
  descripcion?: string;
  ubicacion?: string;
  fechaInicio?: string; // ISO date string
  fechaFinalizacion?: string; // ISO date string
  imagenPrincipal?: string;
  imagenes: string[]; // Array de URLs
  activo: boolean;
  // Campos de aprobación
  clienteId?: number;
  clienteNombre?: string;
  estadoAprobacion?: EstadoAprobacionProyecto;
  motivoRechazo?: string;
  fechaSolicitudAprobacion?: string;
  fechaRespuestaCliente?: string;
}

export interface CrearProyectoRequest {
  nombre: string;
  clienteId: number;
  descripcion?: string;
  ubicacion?: string;
  fechaInicio?: string;
  fechaFinalizacion?: string;
  imagenPrincipal?: File;
  imagenesAdicionales?: File[];
}

export interface AprobacionProyectoRequest {
  proyectoId: number;
  aprobado: boolean;
  motivoRechazo?: string;
}

export interface CrearImagenRequest {
  tipo: TipoImagen;
  titulo?: string;
  descripcion?: string;
  archivo: File;
  orden?: number;
}