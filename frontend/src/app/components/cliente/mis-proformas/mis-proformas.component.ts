import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProformaService } from '../../../services/proforma.service';
import { Proforma, EstadoProforma } from '../../models/proforma.models';
import {
  ESTADOS_PROFORMA_LABELS,
  ESTADOS_PROFORMA_COLORS
} from '../../constants/estados.constants';

@Component({
  selector: 'app-mis-proformas',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-proformas.component.html',
  styleUrl: './mis-proformas.component.css'
})
export class MisProformasComponent implements OnInit {

  proformas: Proforma[] = [];
  proformasFiltradas: Proforma[] = [];
  loading = true;
  filtroEstado: EstadoProforma | 'TODOS' = 'TODOS';

  // Constantes para el template
  readonly estadosLabels = ESTADOS_PROFORMA_LABELS;
  readonly estadosColors = ESTADOS_PROFORMA_COLORS;
  readonly EstadoProforma = EstadoProforma;

  constructor(private proformaService: ProformaService) {}

  ngOnInit(): void {
    this.cargarProformas();
  }

  cargarProformas(): void {
    this.loading = true;
    this.proformaService.obtenerMisProformas().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.proformas = response.data;
          this.aplicarFiltro();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar proformas:', error);
        this.loading = false;
      }
    });
  }

  aplicarFiltro(): void {
    if (this.filtroEstado === 'TODOS') {
      this.proformasFiltradas = [...this.proformas];
    } else {
      this.proformasFiltradas = this.proformas.filter(
        p => p.estado === this.filtroEstado
      );
    }
  }

  cambiarFiltro(estado: EstadoProforma | 'TODOS'): void {
    this.filtroEstado = estado;
    this.aplicarFiltro();
  }

  getColorBadge(estado: EstadoProforma): string {
    const colorMap: Record<string, string> = {
      'primary': 'bg-blue-500',
      'info': 'bg-cyan-500',
      'success': 'bg-green-500',
      'danger': 'bg-red-500',
      'warning': 'bg-yellow-500'
    };

    const color = this.estadosColors[estado];
    return colorMap[color] || 'bg-gray-500';
  }

  formatearFecha(fecha: Date | string): string {
    if (!fecha) return '-';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }

  formatearMoneda(monto: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(monto);
  }

  esVigente(vigenciaHasta: Date | string): boolean {
    if (!vigenciaHasta) return false;
    const fecha = new Date(vigenciaHasta);
    return fecha >= new Date();
  }

  contarPorEstado(estado: EstadoProforma): number {
    return this.proformas.filter(p => p.estado === estado).length;
  }
}
