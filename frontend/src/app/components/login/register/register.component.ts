import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // 👈 IMPORTANTE
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registroForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.registroForm = this.fb.group({
      tipoCliente: ['', Validators.required],
      nombres: [''],
      apellidos: [''],
      razonSocial: [''],
      correoElectronico: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.registroForm.get('tipoCliente')?.valueChanges.subscribe((tipo) => {
      if (tipo === 'natural') {
        this.registroForm.get('nombres')?.setValidators([Validators.required]);
        this.registroForm.get('apellidos')?.setValidators([Validators.required]);
        this.registroForm.get('razonSocial')?.clearValidators();
      } else if (tipo === 'juridica') {
        this.registroForm.get('razonSocial')?.setValidators([Validators.required]);
        this.registroForm.get('nombres')?.clearValidators();
        this.registroForm.get('apellidos')?.clearValidators();
      }

      this.registroForm.get('nombres')?.updateValueAndValidity();
      this.registroForm.get('apellidos')?.updateValueAndValidity();
      this.registroForm.get('razonSocial')?.updateValueAndValidity();
    });
  }

  onSubmit() {
    if (this.registroForm.valid) {
      console.log('✅ Datos registrados:', this.registroForm.value);
      alert('Registro exitoso ✅');
      this.registroForm.reset();
    }
  }
}
