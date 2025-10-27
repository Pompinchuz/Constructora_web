import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import {
  trigger, transition, style, animate, query, stagger
} from '@angular/animations';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(6px)' }),
        animate('500ms ease-out', style({ opacity: 1, transform: 'none' }))
      ])
    ]),
    trigger('fadeUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(14px)' }),
        animate('500ms 120ms ease-out', style({ opacity: 1, transform: 'none' }))
      ])
    ]),
    trigger('staggerList', [
      transition(':enter', [
        query('.feat', [
          style({ opacity: 0, transform: 'translateY(10px)' }),
          stagger(90, animate('420ms ease-out', style({ opacity: 1, transform: 'none' })))
        ])
      ])
    ])
  ]
})
export class HomeComponent {
  year = new Date().getFullYear();

  
  features = [
    { icon: 'verified', title: '100% Clientes', desc: 'Satisfechos' },
    { icon: 'autorenew', title: 'Renovaciones', desc: 'y mejoras' },
    { icon: 'schedule', title: 'Horarios', desc: 'fijos' },
    { icon: 'view_in_ar', title: 'Otros detalles', desc: 'a medida' },
    { icon: 'home_work', title: 'Construcciones', desc: 'destacables' },
    { icon: 'campaign', title: 'Novedades', desc: 'constantes' }
  ];

  services = [
    { 
      icon: 'crane', 
      title: 'Ingeniería Estructural', 
      desc: 'Cálculo y diseño estructural para proyectos de cualquier escala.',
      image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRy_l5TVjyWnD4ixNA5we_VWvM_FQem9fBVZA&s'  
    },
    { 
      icon: 'architecture', 
      title: 'Proformas', 
      desc: 'Análisis de proformas + costos.',
      image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRy_l5TVjyWnD4ixNA5we_VWvM_FQem9fBVZA&s'  
    },
    { 
      icon: 'handyman', 
      title: 'Remodelaciones', 
      desc: 'Reforzamiento y adecuaciones con estándares de calidad.',
      image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRy_l5TVjyWnD4ixNA5we_VWvM_FQem9fBVZA&s'  
    },
    { 
      icon: 'health_and_safety', 
      title: 'Seguridad', 
      desc: 'Cumplimiento normativo y buenas prácticas.',
      image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRy_l5TVjyWnD4ixNA5we_VWvM_FQem9fBVZA&s'  
    }
  ];
}
