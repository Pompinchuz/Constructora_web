// ============================================
// SERVICIOS.COMPONENT.TS - Simplificado con imágenes dinámicas
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ContenidoService } from '../../../services/contenido.service';
import { environment } from '../../../../environments/environment';
import { TipoImagen, Imagen } from '../../models/contenido.models';

interface ServicioSimple {
  titulo: string;
  descripcion: string;
  imagen: string;
}

@Component({
  selector: 'app-servicios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './servicios.component.html',
  styleUrl: './servicios.component.css'
})
export class ServiciosComponent implements OnInit {
  readonly apiUrl = environment.apiUrl;
  servicios: ServicioSimple[] = [];
  loading = true;

  constructor(
    private router: Router,
    private contenidoService: ContenidoService
  ) {}

  ngOnInit(): void {
    this.cargarServicios();
  }

  cargarServicios(): void {
    this.loading = true;
    this.contenidoService.obtenerImagenesActivasPorTipo(TipoImagen.SERVICIO).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          // Convertir imágenes del backend a servicios simples
          this.servicios = response.data.map(img => ({
            titulo: img.titulo || 'Servicio',
            descripcion: img.descripcion || '',
            imagen: this.getFullImageUrl(img.urlImagen)
          }));
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error cargando servicios:', error);
        this.loading = false;
      }
    });
  }

  getFullImageUrl(urlImagen: string): string {
    if (urlImagen && urlImagen.startsWith('http')) {
      return urlImagen;
    }
    return this.apiUrl + urlImagen;
  }

  solicitarProforma(): void {
    this.router.navigate(['/registro-tipo']);
  }
}
