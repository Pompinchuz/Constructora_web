// ============================================
// PROYECTOS-LIST.COMPONENT.TS - Admin Panel
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../../services/proyecto.service';
import { ProyectoExitoso } from '../../models/contenido.models';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-proyectos-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './proyectos-list.component.html',
  styleUrl: './proyectos-list.component.css'
})
export class ProyectosListComponent implements OnInit {
  proyectos: ProyectoExitoso[] = [];
  loading = true;
  readonly apiUrl = environment.apiUrl;

  constructor(private proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos(): void {
    this.loading = true;
    this.proyectoService.obtenerTodosProyectos().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          // Construir URLs completas para imágenes
          this.proyectos = response.data.map(proyecto => ({
            ...proyecto,
            imagenPrincipal: proyecto.imagenPrincipal
              ? this.getFullImageUrl(proyecto.imagenPrincipal)
              : undefined,
            imagenes: proyecto.imagenes.map(img => this.getFullImageUrl(img))
          }));
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error cargando proyectos:', error);
        this.loading = false;
      }
    });
  }

  getFullImageUrl(urlImagen: string): string {
    if (urlImagen && urlImagen.startsWith('http')) {
      return urlImagen;
    }
    return this.apiUrl + urlImagen;
  }

  cambiarEstado(id: number, activo: boolean): void {
    if (confirm(`¿Estás seguro de ${activo ? 'activar' : 'desactivar'} este proyecto?`)) {
      this.proyectoService.cambiarEstadoProyecto(id, activo).subscribe({
        next: () => {
          this.cargarProyectos();
        },
        error: (error) => {
          console.error('Error cambiando estado del proyecto:', error);
          alert('Error al cambiar el estado del proyecto');
        }
      });
    }
  }

  eliminarProyecto(id: number): void {
    if (confirm('¿Estás seguro de eliminar este proyecto? Esta acción no se puede deshacer.')) {
      this.proyectoService.eliminarProyecto(id).subscribe({
        next: () => {
          this.cargarProyectos();
          alert('Proyecto eliminado exitosamente');
        },
        error: (error) => {
          console.error('Error eliminando proyecto:', error);
          alert('Error al eliminar el proyecto');
        }
      });
    }
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';

    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }
}
