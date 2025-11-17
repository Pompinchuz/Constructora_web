// ============================================
// SOBRE-NOSOTROS.COMPONENT.TS - Con imágenes dinámicas
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContenidoService } from '../../../services/contenido.service';
import { environment } from '../../../../environments/environment';

interface Valor {
  icono: string;
  titulo: string;
  descripcion: string;
}

@Component({
  selector: 'app-sobre-nosotros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sobre-nosotros.component.html',
  styleUrl: './sobre-nosotros.component.css'
})
export class SobreNosotrosComponent implements OnInit {
  readonly apiUrl = environment.apiUrl;
  imagenesSobreNosotros: string[] = [];
  
  valores: Valor[] = [
    {
      icono: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z',
      titulo: 'Compromiso',
      descripcion: 'Cumplimos con los plazos y especificaciones acordadas en cada proyecto'
    },
    {
      icono: 'M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z',
      titulo: 'Calidad',
      descripcion: 'Utilizamos materiales de primera calidad y procesos certificados'
    },
    {
      icono: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
      titulo: 'Profesionalismo',
      descripcion: 'Contamos con un equipo altamente capacitado y con experiencia'
    },
    {
      icono: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
      titulo: 'Transparencia',
      descripcion: 'Presupuestos claros y detallados sin costos ocultos'
    }
  ];

  estadisticas = [
    { numero: '10+', label: 'Años de Experiencia' },
    { numero: '150+', label: 'Proyectos Completados' },
    { numero: '98%', label: 'Clientes Satisfechos' },
    { numero: '24/7', label: 'Atención al Cliente' }
  ];

  constructor(private contenidoService: ContenidoService) {}

  ngOnInit(): void {
    this.cargarImagenesSobreNosotros();
  }

  cargarImagenesSobreNosotros(): void {
    this.contenidoService.obtenerImagenesActivasPorTipo('SOBRE_NOSOTROS').subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.imagenesSobreNosotros = response.data.map(img => this.getFullImageUrl(img.urlImagen));
        }
      },
      error: (error) => {
        console.error('Error cargando imágenes de sobre nosotros:', error);
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