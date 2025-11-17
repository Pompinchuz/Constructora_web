// ============================================
// SERVICIOS.COMPONENT.TS - Con imágenes dinámicas
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ContenidoService } from '../../../services/contenido.service';
import { environment } from '../../../../environments/environment';
import { TipoImagen } from '../../models/contenido.models';

interface Servicio {
  icono: string;
  titulo: string;
  descripcion: string;
  caracteristicas: string[];
  imagen?: string; // Imagen dinámica desde backend
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
  imagenesServicio: string[] = [];
  
  servicios: Servicio[] = [
    {
      icono: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z',
      titulo: 'Proformas Detalladas',
      descripcion: 'Presupuestos completos y transparentes para tu proyecto de construcción',
      caracteristicas: [
        'Análisis detallado de costos',
        'Desglose por partidas',
        'Tiempos de ejecución',
        'Materiales especificados'
      ]
    },
    {
      icono: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4',
      titulo: 'Construcción',
      descripcion: 'Construcción de edificaciones residenciales y comerciales',
      caracteristicas: [
        'Casas y departamentos',
        'Locales comerciales',
        'Supervisión constante',
        'Garantía de obra'
      ]
    },
    {
      icono: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z M15 12a3 3 0 11-6 0 3 3 0 016 0z',
      titulo: 'Remodelación',
      descripcion: 'Renovamos y mejoramos espacios existentes',
      caracteristicas: [
        'Remodelación integral',
        'Actualización de espacios',
        'Cambio de acabados',
        'Ampliaciones'
      ]
    },
    {
      icono: 'M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01',
      titulo: 'Asesoría y Planos',
      descripcion: 'Diseño arquitectónico y elaboración de planos técnicos',
      caracteristicas: [
        'Planos arquitectónicos',
        'Planos estructurales',
        'Licencias de construcción',
        'Asesoría técnica'
      ]
    }
  ];

  constructor(
    private router: Router,
    private contenidoService: ContenidoService
  ) {}

  ngOnInit(): void {
    this.cargarImagenesServicio();
  }

  cargarImagenesServicio(): void {
    this.contenidoService.obtenerImagenesActivasPorTipo(TipoImagen.SERVICIO).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          // Asignar imágenes a los servicios
          this.imagenesServicio = response.data.map(img => this.getFullImageUrl(img.urlImagen));

          // Asignar imágenes a cada servicio (según el orden)
          this.servicios.forEach((servicio, index) => {
            if (this.imagenesServicio[index]) {
              servicio.imagen = this.imagenesServicio[index];
            }
          });
        }
      },
      error: (error) => {
        console.error('Error cargando imágenes de servicios:', error);
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