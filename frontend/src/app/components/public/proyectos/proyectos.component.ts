// ============================================
// PROYECTOS.COMPONENT.TS
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../../services/proyecto.service';
import { ProyectoExitoso } from '../../models/contenido.models';

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

  constructor(private proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos(): void {
    this.proyectoService.obtenerProyectosActivos().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.proyectos = response.data;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error cargando proyectos:', error);
        this.loading = false;
      }
    });
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