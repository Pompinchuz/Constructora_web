// ============================================
// ADMIN-DASHBOARD.COMPONENT.TS
// ============================================

// src/app/components/admin/admin-dashboard/admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SolicitudService } from '../../../services/solicitud.service';
import { ProformaService } from '../../../services/proforma.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  estadisticas = {
    solicitudesPendientes: 0,
    totalProformas: 0,
    proformasPagadas: 0,
    montoFacturado: 0
  };
  
  loading = true;

  constructor(
    private solicitudService: SolicitudService,
    private proformaService: ProformaService
  ) {}

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.loading = true;

    // Cargar solicitudes pendientes
    this.solicitudService.contarSolicitudesPendientes().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.estadisticas.solicitudesPendientes = response.data;
        }
      }
    });

    // Cargar estadísticas de proformas
    this.proformaService.obtenerEstadisticas().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.estadisticas.totalProformas = response.data.totalProformas || 0;
          this.estadisticas.proformasPagadas = response.data.proformasPagadas || 0;
          this.estadisticas.montoFacturado = response.data.montoFacturado || 0;
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
}