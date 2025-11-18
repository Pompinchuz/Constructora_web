// ============================================
// SOLICITUD-DETAIL.COMPONENT.TS (Admin)
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SolicitudService } from '../../../services/solicitud.service';
import { SolicitudProforma, EstadoSolicitud } from '../../models/solicitud.models';
import { ESTADOS_SOLICITUD_LABELS } from '../../constants/estados.constants';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-solicitud-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './solicitud-detail.component.html',
  styleUrl: './solicitud-detail.component.css'
})
export class SolicitudDetailComponent implements OnInit {
  solicitud: SolicitudProforma | null = null;
  loading = true;
  mostrarModalRechazo = false;
  mostrarModalCambioEstado = false;
  motivoRechazo = '';
  procesandoAccion = false;
  apiUrl = environment.apiUrl;
  estadoSeleccionado: EstadoSolicitud | null = null;

  // Opciones de estado disponibles
  estadosDisponibles = [
    { value: EstadoSolicitud.PENDIENTE, label: 'Pendiente' },
    { value: EstadoSolicitud.EN_REVISION, label: 'En Revisión' },
    { value: EstadoSolicitud.APROBADA, label: 'Aprobada' },
    { value: EstadoSolicitud.RECHAZADA, label: 'Rechazada' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private solicitudService: SolicitudService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarSolicitud(+id);
    }
  }

  cargarSolicitud(id: number): void {
    this.loading = true;
    this.solicitudService.obtenerSolicitudPorId(id).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.solicitud = response.data;
          this.estadoSeleccionado = response.data.estado;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar solicitud:', error);
        this.loading = false;
        alert('Error al cargar la solicitud');
        this.router.navigate(['/admin/solicitudes']);
      }
    });
  }

  volverALaLista(): void {
  this.router.navigate(['/admin/solicitudes']);
}

  aprobarSolicitud(): void {
    if (!this.solicitud) return;

    if (confirm('¿Está seguro de aprobar esta solicitud?')) {
      this.procesandoAccion = true;
      this.solicitudService.aprobarSolicitud(this.solicitud.id).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.solicitud = response.data;
            alert('Solicitud aprobada exitosamente');
          }
          this.procesandoAccion = false;
        },
        error: (error) => {
          console.error('Error al aprobar:', error);
          alert('Error al aprobar la solicitud');
          this.procesandoAccion = false;
        }
      }
    });
  }

  cancelarCambioEstado(): void {
    // Revertir la selección al estado actual
    if (this.solicitud) {
      this.estadoSeleccionado = this.solicitud.estado;
    }
    this.mostrarModalCambioEstado = false;
  }

  abrirModalRechazo(): void {
    this.mostrarModalRechazo = true;
    this.motivoRechazo = '';
  }

  cerrarModalRechazo(): void {
    this.mostrarModalRechazo = false;
    this.motivoRechazo = '';
    // Revertir el estado seleccionado si se cancela el rechazo
    if (this.solicitud) {
      this.estadoSeleccionado = this.solicitud.estado;
    }
  }

  rechazarSolicitud(): void {
    if (!this.solicitud || !this.motivoRechazo.trim()) {
      alert('Debe proporcionar un motivo de rechazo');
      return;
    }

    this.procesandoAccion = true;
    this.solicitudService.cambiarEstado(
      this.solicitud.id,
      EstadoSolicitud.RECHAZADA,
      this.motivoRechazo
    ).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.solicitud = response.data;
          this.estadoSeleccionado = response.data.estado;
          this.cerrarModalRechazo();
          alert('Solicitud rechazada exitosamente');
        }
        this.procesandoAccion = false;
      },
      error: (error) => {
        console.error('Error al rechazar:', error);
        alert('Error al rechazar la solicitud');
        this.procesandoAccion = false;
        // Revertir el estado seleccionado
        if (this.solicitud) {
          this.estadoSeleccionado = this.solicitud.estado;
        }
      }
    });
  }

  getEstadoLabel(estado: string): string {
    return ESTADOS_SOLICITUD_LABELS[estado as keyof typeof ESTADOS_SOLICITUD_LABELS] || estado;
  }

  getEstadoClass(estado: string): string {
    const classes: { [key: string]: string } = {
      'PENDIENTE': 'bg-yellow-100 text-yellow-800',
      'EN_REVISION': 'bg-blue-100 text-blue-800',
      'APROBADA': 'bg-green-100 text-green-800',
      'RECHAZADA': 'bg-red-100 text-red-800'
    };
    return classes[estado] || 'bg-gray-100 text-gray-800';
  }

  descargarArchivo(): void {
    if (this.solicitud?.archivoAdjunto) {
      const url = `${this.apiUrl}/uploads/solicitudes/${this.solicitud.archivoAdjunto}`;
      window.open(url, '_blank');
    }
  }
}
