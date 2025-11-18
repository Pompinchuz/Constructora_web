// ============================================
// NUEVA-SOLICITUD.COMPONENT.TS (Cliente)
// ============================================

// src/app/components/cliente/nueva-solicitud/nueva-solicitud.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService } from '../../../services/solicitud.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-nueva-solicitud',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './nueva-solicitud.component.html',
  styleUrl: './nueva-solicitud.component.css'
})
export class NuevaSolicitudComponent {
  solicitudForm: FormGroup;
  archivoSeleccionado: File | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private solicitudService: SolicitudService,
    private notificationService: NotificationService,
    private router: Router
  ) {
    this.solicitudForm = this.fb.group({
      titulo: ['', Validators.required],
      descripcion: ['', Validators.required]
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      // Validar tamaño (10MB)
      if (file.size > 10 * 1024 * 1024) {
        this.notificationService.error('El archivo no debe superar 10MB');
        return;
      }
      this.archivoSeleccionado = file;
    }
  }

  onSubmit(): void {
    if (this.solicitudForm.invalid) return;

    this.loading = true;
    const formData = new FormData();
    formData.append('titulo', this.solicitudForm.get('titulo')?.value);
    formData.append('descripcion', this.solicitudForm.get('descripcion')?.value);
    
    if (this.archivoSeleccionado) {
      formData.append('archivo', this.archivoSeleccionado);
    }

    this.solicitudService.crearSolicitud(formData).subscribe({
      next: (response) => {
        if (response.success) {
          this.notificationService.success('Solicitud enviada correctamente');
          this.router.navigate(['/cliente/mis-solicitudes']);
        }
      },
      error: () => {
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/cliente/dashboard']);
  }

  get titulo() { return this.solicitudForm.get('titulo'); }
  get descripcion() { return this.solicitudForm.get('descripcion'); }
}