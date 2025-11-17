// ============================================
// MIS-SOLICITUDES.COMPONENT.TS (Cliente)
// ============================================

// src/app/components/cliente/mis-solicitudes/mis-solicitudes.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SolicitudService } from '../../../services/solicitud.service';
import { ESTADOS_SOLICITUD_LABELS, ESTADOS_SOLICITUD_COLORS } from '../../constants/estados.constants';

@Component({
  selector: 'app-mis-solicitudes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="p-6 max-w-7xl mx-auto">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-900">Mis Solicitudes</h1>
        <a routerLink="/cliente/nueva-solicitud" 
           class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
          + Nueva Solicitud
        </a>
      </div>

      <div *ngIf="loading" class="text-center py-12">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>

      <div *ngIf="!loading && solicitudes.length === 0" class="text-center py-12 bg-white rounded-lg shadow">
        <p class="text-gray-500 mb-4">No tienes solicitudes aún</p>
        <a routerLink="/cliente/nueva-solicitud" class="text-blue-600 hover:underline">
          Crear primera solicitud →
        </a>
      </div>

      <div *ngIf="!loading && solicitudes.length > 0" class="space-y-4">
        <div *ngFor="let solicitud of solicitudes" 
             class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex justify-between items-start">
            <div class="flex-1">
              <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ solicitud.titulo }}</h3>
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ solicitud.descripcion }}</p>
              <div class="flex items-center space-x-4 text-sm text-gray-500">
                <span>📅 {{ solicitud.fechaSolicitud | date:'dd/MM/yyyy' }}</span>
                <span *ngIf="solicitud.archivoAdjunto">📎 Archivo adjunto</span>
              </div>
            </div>
            <div class="ml-4">
              <span class="px-3 py-1 text-xs font-semibold rounded-full"
                    [ngClass]="{
                      'bg-yellow-100 text-yellow-800': solicitud.estado === 'PENDIENTE',
                      'bg-blue-100 text-blue-800': solicitud.estado === 'EN_REVISION',
                      'bg-green-100 text-green-800': solicitud.estado === 'APROBADA',
                      'bg-red-100 text-red-800': solicitud.estado === 'RECHAZADA'
                    }">
                {{ getEstadoLabel(solicitud.estado) }}
              </span>
            </div>
          </div>
          
          <div *ngIf="solicitud.motivoRechazo" class="mt-4 p-3 bg-red-50 border border-red-200 rounded">
            <p class="text-sm text-red-800">
              <strong>Motivo de rechazo:</strong> {{ solicitud.motivoRechazo }}
            </p>
          </div>
        </div>
      </div>
    </div>
  `
})
export class MisSolicitudesComponent implements OnInit {
  solicitudes: any[] = [];
  loading = true;

  constructor(private solicitudService: SolicitudService) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.solicitudService.obtenerMisSolicitudes().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.solicitudes = response.data;
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  getEstadoLabel(estado: string): string {
    return ESTADOS_SOLICITUD_LABELS[estado as keyof typeof ESTADOS_SOLICITUD_LABELS] || estado;
  }
}