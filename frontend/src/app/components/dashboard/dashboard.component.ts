import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  solicitudes = [
    { id: 1, nombre: 'Juan Pérez', email: 'juan@mail.com', descripcion: 'Diseño de logo y branding', fecha: '2025-11-10', estado: 'Pendiente' },
    { id: 2, nombre: 'Ana Torres', email: 'ana@mail.com', descripcion: 'Página web corporativa', fecha: '2025-11-08', estado: 'Pendiente' },
  ];

  selectedRequest: any = null;
  partidaForm: FormGroup;
  partidas: any[] = [];

  constructor(private fb: FormBuilder) {
    this.partidaForm = this.fb.group({
      descripcion: ['', Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precio: [0, [Validators.required, Validators.min(0)]],
    });
  }

  seleccionarSolicitud(solicitud: any) {
    this.selectedRequest = solicitud;
    this.partidas = [];
  }

  agregarPartida() {
    if (this.partidaForm.valid) {
      this.partidas.push(this.partidaForm.value);
      this.partidaForm.reset({ cantidad: 1, precio: 0 });
    }
  }

  calcularTotal() {
    return this.partidas.reduce((total, p) => total + p.cantidad * p.precio, 0);
  }

  aprobar() {
    if (this.selectedRequest) {
      this.selectedRequest.estado = 'Aprobada';
      alert('Solicitud aprobada');
    }
  }

  rechazar() {
    if (this.selectedRequest) {
      this.selectedRequest.estado = 'Rechazada';
      alert('Solicitud rechazada');
    }
  }

  enviarProforma() {
    if (this.selectedRequest) {
      alert(`Proforma enviada al correo de ${this.selectedRequest.email}`);
    }
  }
}
