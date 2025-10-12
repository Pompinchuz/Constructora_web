import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente-form',
  templateUrl: './cliente-form.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class ClienteFormComponent implements OnInit {

  clienteForm: FormGroup;
  userRole: string | null = null;

  constructor(private fb: FormBuilder, private clienteService: ClienteService, private authService: AuthService) {
    // Aquí inicializas el formulario usando el FormBuilder INICIALIZADO
    this.clienteForm = this.fb.group({
      nombre: ['', Validators.required],
      documento: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.userRole = this.authService.getUserRole();
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
