// ============================================
// APROBACION PROYECTOS COMPONENT
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProyectoService } from '../../../services/proyecto.service';
import { NotificationService } from '../../../services/notification.service';
import { ProyectoExitoso, EstadoAprobacionProyecto } from '../../models/contenido.models';

@Component({
  selector: 'app-aprobacion-proyectos',
  templateUrl: './aprobacion-proyectos.component.html',
  styleUrls: ['./aprobacion-proyectos.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AprobacionProyectosComponent implements OnInit {

  proyectosPendientes: ProyectoExitoso[] = [];
  todosProyectos: ProyectoExitoso[] = [];
  loading = false;
  vistaActual: 'pendientes' | 'todos' = 'pendientes';

  // Modal para rechazo
  mostrarModalRechazo = false;
  proyectoSeleccionado: ProyectoExitoso | null = null;
  motivoRechazo = '';

  // Enum para usar en template
  EstadoAprobacion = EstadoAprobacionProyecto;

  constructor(
    private proyectoService: ProyectoService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.cargarProyectosPendientes();
  }

  cargarProyectosPendientes(): void {
    this.loading = true;
    this.proyectoService.obtenerProyectosPendientes().subscribe({
      next: (response) => {
        this.proyectosPendientes = response.data || [];
        this.loading = false;
      },
      error: (error) => {
        this.notificationService.error('Error al cargar proyectos pendientes');
        this.loading = false;
      }
    });
  }

  cargarTodosProyectos(): void {
    this.loading = true;
    this.proyectoService.obtenerMisProyectos().subscribe({
      next: (response) => {
        this.todosProyectos = response.data || [];
        this.loading = false;
      },
      error: (error) => {
        this.notificationService.error('Error al cargar proyectos');
        this.loading = false;
      }
    });
  }

  cambiarVista(vista: 'pendientes' | 'todos'): void {
    this.vistaActual = vista;
    if (vista === 'pendientes') {
      this.cargarProyectosPendientes();
    } else {
      this.cargarTodosProyectos();
    }
  }

  aprobarProyecto(proyecto: ProyectoExitoso): void {
    if (!confirm(`¿Está seguro de aprobar la publicación del proyecto "${proyecto.nombre}"?`)) {
      return;
    }

    this.proyectoService.aprobarProyecto(proyecto.id).subscribe({
      next: (response) => {
        this.notificationService.success('Proyecto aprobado exitosamente');
        this.cargarProyectosPendientes();
        if (this.vistaActual === 'todos') {
          this.cargarTodosProyectos();
        }
      },
      error: (error) => {
        this.notificationService.success('Error al aprobar el proyecto');
      }
    });
  }

  abrirModalRechazo(proyecto: ProyectoExitoso): void {
    this.proyectoSeleccionado = proyecto;
    this.motivoRechazo = '';
    this.mostrarModalRechazo = true;
  }

  cerrarModalRechazo(): void {
    this.mostrarModalRechazo = false;
    this.proyectoSeleccionado = null;
    this.motivoRechazo = '';
  }

  rechazarProyecto(): void {
    if (!this.proyectoSeleccionado) return;

    if (!this.motivoRechazo || this.motivoRechazo.trim() === '') {
      this.notificationService.error('Debe proporcionar un motivo de rechazo');
      return;
    }

    this.proyectoService.rechazarProyecto(this.proyectoSeleccionado.id, this.motivoRechazo).subscribe({
      next: (response) => {
        this.notificationService.success('Proyecto rechazado');
        this.cerrarModalRechazo();
        this.cargarProyectosPendientes();
        if (this.vistaActual === 'todos') {
          this.cargarTodosProyectos();
        }
      },
      error: (error) => {
        this.notificationService.error('Error al rechazar el proyecto');
      }
    });
  }

  obtenerEstadoBadge(estado?: EstadoAprobacionProyecto): string {
    switch (estado) {
      case EstadoAprobacionProyecto.PENDIENTE_APROBACION:
        return 'badge bg-warning';
      case EstadoAprobacionProyecto.APROBADO:
        return 'badge bg-success';
      case EstadoAprobacionProyecto.RECHAZADO:
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }

  obtenerTextoEstado(estado?: EstadoAprobacionProyecto): string {
    switch (estado) {
      case EstadoAprobacionProyecto.PENDIENTE_APROBACION:
        return 'Pendiente de Aprobación';
      case EstadoAprobacionProyecto.APROBADO:
        return 'Aprobado';
      case EstadoAprobacionProyecto.RECHAZADO:
        return 'Rechazado';
      default:
        return 'Desconocido';
    }
  }
}
