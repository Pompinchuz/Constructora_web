// ============================================
// CONTENIDO-WEB.COMPONENT.TS
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ContenidoService } from '../../../services/contenido.service';
import { ProyectoService } from '../../../services/proyecto.service';
import { NotificationService } from '../../../services/notification.service';
import { Imagen, ProyectoExitoso, TipoImagen } from '../../models/contenido.models';
import { environment } from '../../../../environments/environment';


@Component({
  selector: 'app-contenido-web',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contenido-web.component.html',
  styleUrl: './contenido-web.component.css'
})
export class ContenidoWebComponent implements OnInit {

  // URL base para imágenes
  readonly apiUrl = environment.apiUrl;
  
  // Tab activa
  activeTab: 'imagenes' | 'proyectos' = 'imagenes';
  
  // Imágenes
  imagenes: Imagen[] = [];
  loadingImagenes = false;
  filtroTipoImagen: string = '';
  
  // Proyectos
  proyectos: ProyectoExitoso[] = [];
  loadingProyectos = false;
  
  // Modales
  mostrarModalImagen = false;
  mostrarModalProyecto = false;
  modoEdicion = false;
  
  // Formulario de Imagen
  imagenForm = {
    id: null as number | null,
    tipo: 'PORTADA' as TipoImagen,
    titulo: '',
    descripcion: '',
    orden: 0,
    archivo: null as File | null
  };
  
  // Formulario de Proyecto
  proyectoForm = {
    id: null as number | null,
    nombre: '',
    descripcion: '',
    ubicacion: '',
    fechaInicio: '',
    fechaFinalizacion: '',
    imagenPrincipal: null as File | null,
    imagenesAdicionales: [] as File[]
  };
  
  // Drag & Drop
  isDragging = false;

  constructor(
    private contenidoService: ContenidoService,
    private proyectoService: ProyectoService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.cargarImagenes();
    this.cargarProyectos();
  }

  // ============================================
  // GESTIÓN DE IMÁGENES
  // ============================================

  cargarImagenes(): void {
    this.loadingImagenes = true;
    
    const observable = this.filtroTipoImagen 
      ? this.contenidoService.obtenerImagenesPorTipo(this.filtroTipoImagen as TipoImagen)
      : this.contenidoService.obtenerTodasImagenesPublicas();
    
    observable.subscribe({
      next: (response) => {
        if (response.success) {
          this.imagenes = response.data || [];
        }
        this.loadingImagenes = false;
      },
      error: (error) => {
        console.error('Error cargando imágenes:', error);
        this.notificationService.error('Error al cargar imágenes');
        this.loadingImagenes = false;
      }
    });
  }

  abrirModalImagen(imagen?: Imagen): void {
    if (imagen) {
      this.modoEdicion = true;
      this.imagenForm = {
        id: imagen.id,
        tipo: imagen.tipo,
        titulo: imagen.titulo || '',
        descripcion: imagen.descripcion || '',
        orden: imagen.orden,
        archivo: null
      };
    } else {
      this.modoEdicion = false;
      this.resetFormImagen();
    }
    this.mostrarModalImagen = true;
  }

  cerrarModalImagen(): void {
    this.mostrarModalImagen = false;
    this.resetFormImagen();
  }

  resetFormImagen(): void {
    this.imagenForm = {
      id: null,
      tipo: 'PORTADA' as TipoImagen,
      titulo: '',
      descripcion: '',
      orden: 0,
      archivo: null
    };
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.imagenForm.archivo = file;
    }
  }

  // Drag & Drop
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
    
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.imagenForm.archivo = files[0];
    }
  }

  guardarImagen(): void {
    if (!this.imagenForm.archivo && !this.modoEdicion) {
      this.notificationService.warning('Debes seleccionar un archivo');
      return;
    }

    const formData = new FormData();
    formData.append('tipo', this.imagenForm.tipo);
    formData.append('titulo', this.imagenForm.titulo);
    formData.append('descripcion', this.imagenForm.descripcion);
    formData.append('orden', this.imagenForm.orden.toString());
    
    if (this.imagenForm.archivo) {
      formData.append('archivo', this.imagenForm.archivo);
    }

    if (this.modoEdicion && this.imagenForm.id) {
      // Actualizar (sin archivo)
      this.contenidoService.actualizarImagen(this.imagenForm.id, formData).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Imagen actualizada exitosamente');
            this.cargarImagenes();
            this.cerrarModalImagen();
          }
        },
        error: (error) => {
          console.error('Error actualizando imagen:', error);
          this.notificationService.error('Error al actualizar imagen');
        }
      });
    } else {
      // Crear nueva
      this.contenidoService.subirImagen(formData).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Imagen subida exitosamente');
            this.cargarImagenes();
            this.cerrarModalImagen();
          }
        },
        error: (error) => {
          console.error('Error subiendo imagen:', error);
          this.notificationService.error('Error al subir imagen');
        }
      });
    }
  }

  verImagen(imagen: Imagen): void {
    window.open(imagen.urlImagen, '_blank');
  }

  editarImagen(imagen: Imagen): void {
    this.abrirModalImagen(imagen);
  }

  eliminarImagen(imagen: Imagen): void {
    if (confirm(`¿Estás seguro de eliminar la imagen "${imagen.titulo}"?`)) {
      this.contenidoService.eliminarImagen(imagen.id).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Imagen eliminada');
            this.cargarImagenes();
          }
        },
        error: (error) => {
          console.error('Error eliminando imagen:', error);
          this.notificationService.error('Error al eliminar imagen');
        }
      });
    }
  }

  // ============================================
  // GESTIÓN DE PROYECTOS
  // ============================================

  cargarProyectos(): void {
    this.loadingProyectos = true;
    
    this.proyectoService.obtenerTodosProyectos().subscribe({
      next: (response) => {
        if (response.success) {
          this.proyectos = response.data || [];
        }
        this.loadingProyectos = false;
      },
      error: (error) => {
        console.error('Error cargando proyectos:', error);
        this.notificationService.error('Error al cargar proyectos');
        this.loadingProyectos = false;
      }
    });
  }

  abrirModalProyecto(proyecto?: ProyectoExitoso): void {
    if (proyecto) {
      this.modoEdicion = true;
      this.proyectoForm = {
        id: proyecto.id,
        nombre: proyecto.nombre,
        descripcion: proyecto.descripcion || '',
        ubicacion: proyecto.ubicacion || '',
        fechaInicio: proyecto.fechaInicio || '',
        fechaFinalizacion: proyecto.fechaFinalizacion || '',
        imagenPrincipal: null,
        imagenesAdicionales: []
      };
    } else {
      this.modoEdicion = false;
      this.resetFormProyecto();
    }
    this.mostrarModalProyecto = true;
  }

  cerrarModalProyecto(): void {
    this.mostrarModalProyecto = false;
    this.resetFormProyecto();
  }

  resetFormProyecto(): void {
    this.proyectoForm = {
      id: null,
      nombre: '',
      descripcion: '',
      ubicacion: '',
      fechaInicio: '',
      fechaFinalizacion: '',
      imagenPrincipal: null,
      imagenesAdicionales: []
    };
  }

  onImagenPrincipalSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.proyectoForm.imagenPrincipal = file;
    }
  }

  onImagenesAdicionalesSelected(event: any): void {
    const files = Array.from(event.target.files) as File[];
    this.proyectoForm.imagenesAdicionales = files;
  }

  guardarProyecto(): void {
    if (!this.proyectoForm.nombre.trim()) {
      this.notificationService.warning('El nombre del proyecto es requerido');
      return;
    }

    const formData = new FormData();
    formData.append('nombre', this.proyectoForm.nombre);
    formData.append('descripcion', this.proyectoForm.descripcion);
    formData.append('ubicacion', this.proyectoForm.ubicacion);
    
    if (this.proyectoForm.fechaInicio) {
      formData.append('fechaInicio', this.proyectoForm.fechaInicio);
    }
    if (this.proyectoForm.fechaFinalizacion) {
      formData.append('fechaFinalizacion', this.proyectoForm.fechaFinalizacion);
    }
    if (this.proyectoForm.imagenPrincipal) {
      formData.append('imagenPrincipal', this.proyectoForm.imagenPrincipal);
    }
    
    // Agregar imágenes adicionales
    this.proyectoForm.imagenesAdicionales.forEach((file, index) => {
      formData.append('imagenesAdicionales', file);
    });

    if (this.modoEdicion && this.proyectoForm.id) {
      // Actualizar
      this.proyectoService.actualizarProyecto(this.proyectoForm.id, {
        nombre: this.proyectoForm.nombre,
        descripcion: this.proyectoForm.descripcion,
        ubicacion: this.proyectoForm.ubicacion,
        fechaInicio: this.proyectoForm.fechaInicio,
        fechaFinalizacion: this.proyectoForm.fechaFinalizacion
      }).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Proyecto actualizado');
            this.cargarProyectos();
            this.cerrarModalProyecto();
          }
        },
        error: (error) => {
          console.error('Error actualizando proyecto:', error);
          this.notificationService.error('Error al actualizar proyecto');
        }
      });
    } else {
      // Crear nuevo
      this.proyectoService.crearProyecto(formData).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Proyecto creado exitosamente');
            this.cargarProyectos();
            this.cerrarModalProyecto();
          }
        },
        error: (error) => {
          console.error('Error creando proyecto:', error);
          this.notificationService.error('Error al crear proyecto');
        }
      });
    }
  }

  editarProyecto(proyecto: ProyectoExitoso): void {
    this.abrirModalProyecto(proyecto);
  }

  toggleProyectoActivo(proyecto: ProyectoExitoso): void {
    const nuevoEstado = !proyecto.activo;
    
    this.proyectoService.cambiarEstadoProyecto(proyecto.id, nuevoEstado).subscribe({
      next: (response) => {
        if (response.success) {
          this.notificationService.success(`Proyecto ${nuevoEstado ? 'activado' : 'desactivado'}`);
          this.cargarProyectos();
        }
      },
      error: (error) => {
        console.error('Error cambiando estado:', error);
        this.notificationService.error('Error al cambiar estado');
      }
    });
  }

  eliminarProyecto(proyecto: ProyectoExitoso): void {
    if (confirm(`¿Estás seguro de eliminar el proyecto "${proyecto.nombre}"?`)) {
      this.proyectoService.eliminarProyecto(proyecto.id).subscribe({
        next: (response) => {
          if (response.success) {
            this.notificationService.success('Proyecto eliminado');
            this.cargarProyectos();
          }
        },
        error: (error) => {
          console.error('Error eliminando proyecto:', error);
          this.notificationService.error('Error al eliminar proyecto');
        }
      });
    }
  }

  // ============================================
  // UTILIDADES
  // ============================================

  getFullImageUrl(urlImagen: string): string {
    // Si la URL ya es completa (comienza con http), retornarla tal cual
    if (urlImagen && urlImagen.startsWith('http')) {
      return urlImagen;
    }
    // Si no, construir la URL completa con el dominio del API
    return this.apiUrl + urlImagen;
  }
}