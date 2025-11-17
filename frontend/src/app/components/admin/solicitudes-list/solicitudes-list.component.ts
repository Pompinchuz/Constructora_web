// ============================================
// SOLICITUDES-LIST.COMPONENT.TS (Admin)
// ============================================

// src/app/components/admin/solicitudes-list/solicitudes-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SolicitudService } from '../../../services/solicitud.service';
import { ESTADOS_SOLICITUD_LABELS } from '../../constants/estados.constants';

@Component({
  selector: 'app-solicitudes-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="p-6 max-w-7xl mx-auto">
      <h1 class="text-2xl font-bold text-gray-900 mb-6">Gestión de Solicitudes</h1>

      <!-- Filtros -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">Filtrar por estado:</label>
        <select [(ngModel)]="estadoFiltro" (change)="filtrarPorEstado()" 
                class="px-3 py-2 border border-gray-300 rounded-md">
          <option value="">Todos</option>
          <option value="PENDIENTE">Pendiente</option>
          <option value="EN_REVISION">En Revisión</option>
          <option value="APROBADA">Aprobada</option>
          <option value="RECHAZADA">Rechazada</option>
        </select>
      </div>

      <div *ngIf="loading" class="text-center py-12">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>

      <div *ngIf="!loading" class="space-y-4">
        <div *ngFor="let solicitud of solicitudes" 
             class="bg-white rounded-lg shadow-md p-6">
          <div class="flex justify-between items-start mb-4">
            <div class="flex-1">
              <h3 class="text-lg font-semibold text-gray-900">{{ solicitud.titulo }}</h3>
              <p class="text-sm text-gray-600 mt-1">Cliente: {{ solicitud.clienteNombre }}</p>
              <p class="text-sm text-gray-500 mt-1">{{ solicitud.fechaSolicitud | date:'dd/MM/yyyy HH:mm' }}</p>
            </div>
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

          <p class="text-gray-700 mb-4">{{ solicitud.descripcion }}</p>

          <div class="flex justify-end space-x-3">
            <a [routerLink]="['/admin/solicitudes', solicitud.id]" 
               class="px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700">
              Ver Detalles
            </a>
            <button *ngIf="solicitud.estado === 'PENDIENTE'"
                    (click)="aprobar(solicitud.id)"
                    class="px-4 py-2 text-sm bg-green-600 text-white rounded hover:bg-green-700">
              Aprobar
            </button>
            <button *ngIf="solicitud.estado === 'APROBADA'"
                    [routerLink]="['/admin/crear-proforma', solicitud.id]"
                    class="px-4 py-2 text-sm bg-purple-600 text-white rounded hover:bg-purple-700">
              Crear Proforma
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class SolicitudesListComponent implements OnInit {
  solicitudes: any[] = [];
  estadoFiltro: string = '';
  loading = true;

  constructor(private solicitudService: SolicitudService) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.loading = true;
    this.solicitudService.obtenerTodasSolicitudes(this.estadoFiltro as any).subscribe({
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

  filtrarPorEstado(): void {
    this.cargarSolicitudes();
  }

  aprobar(id: number): void {
    if (confirm('¿Aprobar esta solicitud?')) {
      this.solicitudService.aprobarSolicitud(id).subscribe({
        next: () => {
          this.cargarSolicitudes();
        }
      });
    }
  }

  getEstadoLabel(estado: string): string {
    return ESTADOS_SOLICITUD_LABELS[estado as keyof typeof ESTADOS_SOLICITUD_LABELS] || estado;
  }
}