import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProformaService } from '../../../services/proforma.service';
import { ComprobantePagoService } from '../../../services/comprobante-pago.service';
import { Proforma } from '../../models/proforma.models';

@Component({
  selector: 'app-subir-comprobante',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './subir-comprobante.component.html',
  styleUrl: './subir-comprobante.component.css'
})
export class SubirComprobanteComponent implements OnInit {

  comprobanteForm!: FormGroup;
  proforma: Proforma | null = null;
  proformaId!: number;
  loading = false;
  loadingProforma = true;
  error = false;
  archivoSeleccionado: File | null = null;
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private proformaService: ProformaService,
    private comprobanteService: ComprobantePagoService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.proformaId = +params['proformaId'];
      if (this.proformaId) {
        this.cargarProforma();
      } else {
        this.error = true;
        this.loadingProforma = false;
      }
    });

    this.inicializarFormulario();
  }

  inicializarFormulario(): void {
    this.comprobanteForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(0.01)]],
      numeroOperacion: [''],
      entidadBancaria: [''],
      observaciones: ['']
    });
  }

  cargarProforma(): void {
    this.loadingProforma = true;
    this.proformaService.obtenerProformaPorId(this.proformaId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.proforma = response.data;
          // Pre-llenar el monto con el total de la proforma
          this.comprobanteForm.patchValue({
            monto: this.proforma.total
          });
        } else {
          this.error = true;
        }
        this.loadingProforma = false;
      },
      error: (err) => {
        console.error('Error al cargar proforma:', err);
        this.error = true;
        this.loadingProforma = false;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Validar tipo de archivo
      const tiposPermitidos = ['image/jpeg', 'image/jpg', 'image/png', 'application/pdf'];
      if (!tiposPermitidos.includes(file.type)) {
        alert('Solo se permiten archivos JPG, PNG o PDF');
        input.value = '';
        return;
      }

      // Validar tamaño (5MB máximo)
      const maxSize = 5 * 1024 * 1024;
      if (file.size > maxSize) {
        alert('El archivo no debe superar 5MB');
        input.value = '';
        return;
      }

      this.archivoSeleccionado = file;

      // Crear preview si es imagen
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.previewUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        this.previewUrl = null;
      }
    }
  }

  onSubmit(): void {
    if (this.comprobanteForm.valid && this.archivoSeleccionado) {
      this.loading = true;

      const formData = this.comprobanteForm.value;

      this.comprobanteService.subirComprobante(
        this.proformaId,
        formData.monto,
        this.archivoSeleccionado,
        formData.numeroOperacion,
        formData.entidadBancaria,
        formData.observaciones
      ).subscribe({
        next: (response) => {
          if (response.success) {
            alert('Comprobante subido exitosamente. Tu pago será verificado pronto.');
            this.router.navigate(['/cliente/mis-proformas']);
          }
          this.loading = false;
        },
        error: (err) => {
          console.error('Error al subir comprobante:', err);
          alert('Error al subir comprobante. Por favor, intenta nuevamente.');
          this.loading = false;
        }
      });
    } else {
      if (!this.archivoSeleccionado) {
        alert('Por favor, selecciona un archivo del comprobante');
      } else {
        alert('Por favor, completa todos los campos requeridos');
      }
    }
  }

  cancelar(): void {
    this.router.navigate(['/cliente/proforma', this.proformaId]);
  }

  formatearMoneda(monto: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(monto);
  }

  // Getters para validación
  get monto() {
    return this.comprobanteForm.get('monto');
  }
}
