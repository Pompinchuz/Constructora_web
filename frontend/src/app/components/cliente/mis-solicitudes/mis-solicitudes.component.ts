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
  templateUrl: './mis-solicitudes.component.html',
  styleUrl: './mis-solicitudes.component.css'
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