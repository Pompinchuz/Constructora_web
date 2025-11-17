// ============================================
// REGISTRO-TIPO.COMPONENT.TS
// ============================================

// src/app/components/auth/registro-tipo/registro-tipo.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-registro-tipo',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './registro-tipo.component.html',
  styleUrl: './registro-tipo.component.css'
})
export class RegistroTipoComponent {
  constructor(private router: Router) {}

  seleccionarTipo(tipo: 'natural' | 'juridico'): void {
    this.router.navigate(['/register', tipo]);
  }
}