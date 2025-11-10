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
    fechaSubida: Date;
  }
  
  export interface ProyectoExitoso {
    id: number;
    nombre: string;
    descripcion?: string;
    ubicacion?: string;
    fechaInicio?: Date;
    fechaFinalizacion?: Date;
    imagenPrincipal?: string;
    imagenes: string[];
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