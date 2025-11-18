import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProformaService } from '../../../services/proforma.service';
import { Proforma, EstadoProforma, GastoProforma } from '../../models/proforma.models';
import {
  ESTADOS_PROFORMA_LABELS,
  ESTADOS_PROFORMA_COLORS
} from '../../constants/estados.constants';

@Component({
  selector: 'app-proforma-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './proforma-detail.component.html',
  styleUrl: './proforma-detail.component.css'
})
export class ProformaDetailComponent implements OnInit {

  proforma: Proforma | null = null;
  loading = true;
  error = false;
  proformaId!: number;

  // Constantes para el template
  readonly estadosLabels = ESTADOS_PROFORMA_LABELS;
  readonly estadosColors = ESTADOS_PROFORMA_COLORS;
  readonly EstadoProforma = EstadoProforma;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private proformaService: ProformaService
  ) {}

  ngOnInit(): void {
    // Obtener ID de la ruta
    this.route.params.subscribe(params => {
      this.proformaId = +params['id'];
      if (this.proformaId) {
        this.cargarProforma();
      } else {
        this.error = true;
        this.loading = false;
      }
    });
  }

  cargarProforma(): void {
    this.loading = true;
    this.error = false;

    this.proformaService.obtenerProformaPorId(this.proformaId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.proforma = response.data;

          // Marcar como vista si está en estado ENVIADA
          if (this.proforma.estado === EstadoProforma.ENVIADA) {
            this.marcarComoVista();
          }
        } else {
          this.error = true;
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar proforma:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  marcarComoVista(): void {
    this.proformaService.marcarComoVista(this.proformaId).subscribe({
      next: () => {
        console.log('Proforma marcada como vista');
        // Actualizar el estado local
        if (this.proforma) {
          this.proforma.estado = EstadoProforma.VISTA;
        }
      },
      error: (err) => {
        console.error('Error al marcar como vista:', err);
      }
    });
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
      month: 'long',
      day: 'numeric'
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

  calcularSubtotalGasto(gasto: GastoProforma): number {
    return gasto.cantidad * gasto.precioUnitario;
  }

  volverALista(): void {
    this.router.navigate(['/cliente/mis-proformas']);
  }

  irASubirComprobante(): void {
    this.router.navigate(['/cliente/subir-comprobante', this.proformaId]);
  }

  // Método para imprimir (futuro)
  imprimirProforma(): void {
    window.print();
  }
}
