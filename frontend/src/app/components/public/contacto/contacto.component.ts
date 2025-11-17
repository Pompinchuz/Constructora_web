// ============================================
// CONTACTO.COMPONENT.TS
// ============================================

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contacto.component.html',
  styleUrl: './contacto.component.css'
})
export class ContactoComponent implements OnInit {
  contactoForm!: FormGroup;
  isAuthenticated = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    // Verificar si el usuario está autenticado
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = !!user;
    });

    this.initForm();
  }

  initForm(): void {
    this.contactoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      mensaje: ['', [Validators.required, Validators.minLength(20)]]
    });
  }

  get f() {
    return this.contactoForm.controls;
  }

  onSubmit(): void {
    if (this.contactoForm.invalid) {
      Object.keys(this.contactoForm.controls).forEach(key => {
        this.contactoForm.controls[key].markAsTouched();
      });
      return;
    }

    if (!this.isAuthenticated) {
      this.notificationService.info(
        'Para solicitar una proforma debes registrarte primero'
      );
      setTimeout(() => {
        this.router.navigate(['/registro-tipo']);
      }, 2000);
      return;
    }

    // Si está autenticado, redirigir a crear solicitud
    this.router.navigate(['/cliente/nueva-solicitud']);
  }

  irARegistro(): void {
    this.router.navigate(['/registro-tipo']);
  }
}