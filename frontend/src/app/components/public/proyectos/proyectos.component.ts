// ============================================
// PROYECTOS.COMPONENT.TS
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../../services/proyecto.service';
import { ProyectoExitoso } from '../../models/contenido.models';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-proyectos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './proyectos.component.html',
  styleUrl: './proyectos.component.css'
})
export class ProyectosComponent implements OnInit {
  proyectos: ProyectoExitoso[] = [];
  loading = true;
  readonly apiUrl = environment.apiUrl;

  constructor(private proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos(): void {
    this.proyectoService.obtenerProyectosActivos().subscribe({
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

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'Sin fecha';

    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'long'
    });
  }
}