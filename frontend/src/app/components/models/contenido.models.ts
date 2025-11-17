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
}

export interface CrearProyectoRequest {
  nombre: string;
  descripcion?: string;
  ubicacion?: string;
  fechaInicio?: string;
  fechaFinalizacion?: string;
  imagenPrincipal?: File;
  imagenesAdicionales?: File[];
}

export interface CrearImagenRequest {
  tipo: TipoImagen;
  titulo?: string;
  descripcion?: string;
  archivo: File;
  orden?: number;
}