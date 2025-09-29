import { Component } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente-form',
  templateUrl: './cliente-form.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class ClienteFormComponent {

  clienteForm: FormGroup;

  constructor(private fb: FormBuilder, private clienteService: ClienteService) {
    // Aquí inicializas el formulario usando el FormBuilder INICIALIZADO
    this.clienteForm = this.fb.group({
      nombre: ['', Validators.required],
      documento: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.clienteForm.valid) {
      this.clienteService.crearCliente(this.clienteForm.value).subscribe({
        next: () => {
          alert('Cliente registrado correctamente');
          this.clienteForm.reset();
        },
        error: (err) => {
          alert('Error al registrar cliente');
          console.error(err);
        }
      });
    }
  }
}
