// ============================================
// CLIENTE-DASHBOARD.COMPONENT.TS
// ============================================

// src/app/components/cliente/cliente-dashboard/cliente-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SolicitudService } from '../../../services/solicitud.service';
import { ProformaService } from '../../../services/proforma.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-cliente-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cliente-dashboard.component.html',
  styleUrl: './cliente-dashboard.component.css'
})
export class ClienteDashboardComponent implements OnInit {
  usuario: any;
  estadisticas = {
    totalSolicitudes: 0,
    solicitudesPendientes: 0,
    totalProformas: 0,
    proformasPendientes: 0
  };
  
  ultimasSolicitudes: any[] = [];
  ultimasProformas: any[] = [];
  loading = true;

  constructor(
    private authService: AuthService,
    private solicitudService: SolicitudService,
    private proformaService: ProformaService
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.getCurrentUser();
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.loading = true;

    // Cargar mis solicitudes
    this.solicitudService.obtenerMisSolicitudes().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.estadisticas.totalSolicitudes = response.data.length;
          this.estadisticas.solicitudesPendientes = response.data.filter(
            (s: any) => s.estado === 'PENDIENTE' || s.estado === 'EN_REVISION'
          ).length;
          this.ultimasSolicitudes = response.data.slice(0, 3);
        }
      }
    });

    // Cargar mis proformas
    this.proformaService.obtenerMisProformas().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.estadisticas.totalProformas = response.data.length;
          this.estadisticas.proformasPendientes = response.data.filter(
            (p: any) => p.estado === 'ENVIADA' || p.estado === 'VISTA'
          ).length;
          this.ultimasProformas = response.data.slice(0, 3);
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
}