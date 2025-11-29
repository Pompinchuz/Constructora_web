// ============================================
// GALERIA.COMPONENT.TS - Galería de imágenes
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContenidoService } from '../../../services/contenido.service';
import { environment } from '../../../../environments/environment';
import { TipoImagen } from '../../models/contenido.models';

@Component({
  selector: 'app-galeria',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './galeria.component.html',
  styleUrl: './galeria.component.css'
})
export class GaleriaComponent implements OnInit {
  readonly apiUrl = environment.apiUrl;
  imagenesGaleria: string[] = [];
  loading = false;

  constructor(private contenidoService: ContenidoService) {}

  ngOnInit(): void {
    this.cargarImagenesGaleria();
  }

  cargarImagenesGaleria(): void {
    this.loading = true;
    this.contenidoService.obtenerImagenesActivasPorTipo(TipoImagen.GALERIA).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.imagenesGaleria = response.data.map(img => this.getFullImageUrl(img.urlImagen));
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error cargando galería de imágenes:', error);
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
}
