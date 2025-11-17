// ============================================
// HERO-SECTION.COMPONENT.TS - Con carga dinámica de imágenes
// ============================================

import { Component, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { ContenidoService } from '../../../services/contenido.service';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero-section.component.html',
  styleUrl: './hero-section.component.css'
})
export class HeroSectionComponent implements OnInit {
  private isBrowser: boolean;
  imagenPortada: string = '';
  readonly apiUrl = environment.apiUrl;

  constructor(
    private router: Router,
    private contenidoService: ContenidoService,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    this.cargarImagenPortada();
  }

  cargarImagenPortada(): void {
    this.contenidoService.obtenerImagenesActivasPorTipo('PORTADA').subscribe({
      next: (response) => {
        if (response.success && response.data && response.data.length > 0) {
          // Tomar la primera imagen activa de tipo PORTADA
          const imagen = response.data[0];
          this.imagenPortada = this.getFullImageUrl(imagen.urlImagen);
        }
      },
      error: (error) => {
        console.error('Error cargando imagen de portada:', error);
      }
    });
  }

  getFullImageUrl(urlImagen: string): string {
    if (urlImagen && urlImagen.startsWith('http')) {
      return urlImagen;
    }
    return this.apiUrl + urlImagen;
  }

  scrollToContacto(): void {
    if (this.isBrowser) {
      const element = document.getElementById('contacto');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    }
  }

  irARegistro(): void {
    this.router.navigate(['/registro-tipo']);
  }
}