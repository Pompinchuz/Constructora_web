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
  template: `
    <div class="p-6 max-w-3xl mx-auto">
      <h1 class="text-2xl font-bold text-gray-900 mb-6">Nueva Solicitud de Proforma</h1>
      
      <form [formGroup]="solicitudForm" (ngSubmit)="onSubmit()" class="bg-white rounded-lg shadow-md p-6">
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">Título *</label>
          <input
            type="text"
            formControlName="titulo"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Ej: Construcción de vivienda de 2 pisos"
          />
          <div *ngIf="titulo?.invalid && titulo?.touched" class="mt-1 text-sm text-red-600">
            El título es obligatorio
          </div>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">Descripción del Proyecto *</label>
          <textarea
            formControlName="descripcion"
            rows="6"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Describe detalladamente tu proyecto..."
          ></textarea>
          <div *ngIf="descripcion?.invalid && descripcion?.touched" class="mt-1 text-sm text-red-600">
            La descripción es obligatoria
          </div>
        </div>

        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">Archivo Adjunto (Opcional)</label>
          <input
            type="file"
            (change)="onFileSelected($event)"
            accept=".pdf,.jpg,.jpeg,.png"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <p class="mt-1 text-sm text-gray-500">Formatos: PDF, JPG, PNG (Máx. 10MB)</p>
          <div *ngIf="archivoSeleccionado" class="mt-2 text-sm text-green-600">
            ✓ {{ archivoSeleccionado.name }}
          </div>
        </div>

        <div class="flex justify-end space-x-4">
          <button
            type="button"
            (click)="cancelar()"
            class="px-6 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            [disabled]="loading || solicitudForm.invalid"
            class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
          >
            {{ loading ? 'Enviando...' : 'Enviar Solicitud' }}
          </button>
        </div>
      </form>
    </div>
  `
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