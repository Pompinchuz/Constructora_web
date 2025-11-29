// ============================================
// SOLICITUD-DETAIL.COMPONENT.TS (Admin)
// Métodos agregados: onEstadoChange() y confirmarCambioEstado()
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
  estadoAnterior: EstadoSolicitud | null = null; // ✅ Para guardar el estado anterior

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
          this.estadoAnterior = response.data.estado; // ✅ Guardar estado actual
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

  // ============================================
  // ✅ NUEVO MÉTODO: onEstadoChange()
  // ============================================
  /**
   * Se ejecuta cuando el usuario cambia el estado en el select
   * Detecta si se seleccionó RECHAZADA para mostrar modal de motivo
   * Para otros estados, muestra modal de confirmación
   */
  onEstadoChange(): void {
    if (!this.solicitud || !this.estadoSeleccionado) return;

    // Si el estado no ha cambiado, no hacer nada
    if (this.estadoSeleccionado === this.estadoAnterior) {
      return;
    }

    // Si se selecciona RECHAZADA, mostrar modal de rechazo
    if (this.estadoSeleccionado === EstadoSolicitud.RECHAZADA) {
      this.abrirModalRechazo();
    } else {
      // Para otros estados, mostrar modal de confirmación
      this.mostrarModalCambioEstado = true;
    }
  }

  // ============================================
  // ✅ NUEVO MÉTODO: confirmarCambioEstado()
  // ============================================
  /**
   * Confirma el cambio de estado y llama al servicio
   * Se ejecuta cuando el usuario hace clic en "Confirmar" en el modal
   */
  // ============================================
// MÉTODO CORREGIDO: confirmarCambioEstado()
// ============================================
confirmarCambioEstado(): void {
  if (!this.solicitud || !this.estadoSeleccionado) return;

  this.procesandoAccion = true;
  
  // ✅ CORRECCIÓN: Agregar 'undefined' como tercer parámetro
  this.solicitudService.cambiarEstado(
    this.solicitud.id, 
    this.estadoSeleccionado, 
    undefined  // ← motivoRechazo es undefined para estados que no sean RECHAZADA
  ).subscribe({
    next: (response) => {
      if (response.success && response.data) {
        this.solicitud = response.data;
        this.estadoSeleccionado = response.data.estado;
        this.mostrarModalCambioEstado = false;
        alert(`Estado cambiado a ${this.getEstadoLabel(response.data.estado)} exitosamente`);
      }
      this.procesandoAccion = false;
    },
    error: (error) => {
      console.error('Error al cambiar estado:', error);
      alert('Error al cambiar el estado de la solicitud');
      this.procesandoAccion = false;
      // Revertir al estado original en caso de error
      if (this.solicitud) {
        this.estadoSeleccionado = this.solicitud.estado;
      }
    }
  });
}
  aprobarSolicitud(): void {
    if (!this.solicitud) return;

    if (confirm('¿Está seguro de aprobar esta solicitud?')) {
      this.procesandoAccion = true;
      this.solicitudService.aprobarSolicitud(this.solicitud.id).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.solicitud = response.data;
            this.estadoSeleccionado = response.data.estado;
            this.estadoAnterior = response.data.estado; // ✅ Actualizar
            alert('Solicitud aprobada exitosamente');
          }
          this.procesandoAccion = false;
        },
        error: (error) => {
          console.error('Error al aprobar:', error);
          alert('Error al aprobar la solicitud');
          this.procesandoAccion = false;
        }
      });
    }
  }

  // ============================================
  // ✅ MODIFICADO: cancelarCambioEstado()
  // ============================================
  cancelarCambioEstado(): void {
    // Revertir la selección al estado anterior (no al estado actual de la solicitud)
    this.estadoSeleccionado = this.estadoAnterior;
    this.mostrarModalCambioEstado = false;
  }

  abrirModalRechazo(): void {
    this.mostrarModalRechazo = true;
    this.motivoRechazo = '';
  }

  // ============================================
  // ✅ MODIFICADO: cerrarModalRechazo()
  // ============================================
  cerrarModalRechazo(): void {
    this.mostrarModalRechazo = false;
    this.motivoRechazo = '';
    // Revertir al estado anterior
    this.estadoSeleccionado = this.estadoAnterior;
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
          this.estadoAnterior = response.data.estado; // ✅ Actualizar
          this.cerrarModalRechazo();
          alert('Solicitud rechazada exitosamente');
        }
        this.procesandoAccion = false;
      },
      error: (error) => {
        console.error('Error al rechazar:', error);
        alert('Error al rechazar la solicitud');
        this.procesandoAccion = false;
        // Revertir al estado anterior
        this.estadoSeleccionado = this.estadoAnterior;
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