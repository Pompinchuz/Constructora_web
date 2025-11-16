// ============================================
// REGISTER.COMPONENT.TS
// ============================================

// src/app/components/auth/register/register.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { NotificationService } from '../../../services/notification.service';
import { APP_CONSTANTS } from '../../constants/app-constants';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  showPassword = false;
  showConfirmPassword = false;
  tipoCliente: 'natural' | 'juridico' = 'natural';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    // Obtener tipo de cliente de los parámetros de ruta
    this.route.params.subscribe(params => {
      this.tipoCliente = params['tipo'] || 'natural';
      this.initForm();
    });
  }

  private initForm(): void {
    if (this.tipoCliente === 'natural') {
      this.initFormPersonaNatural();
    } else {
      this.initFormPersonaJuridica();
    }
  }

  private initFormPersonaNatural(): void {
    this.registerForm = this.fb.group({
      correoElectronico: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(APP_CONSTANTS.VALIDATION.PASSWORD_MIN_LENGTH)]],
      confirmarContrasena: ['', [Validators.required]],
      nombres: ['', [Validators.required]],
      apellidos: ['', [Validators.required]],
      dni: ['', [
        Validators.pattern(`^[0-9]{${APP_CONSTANTS.VALIDATION.DNI_LENGTH}}$`)
      ]],
      telefono: ['', [
        Validators.pattern(`^[0-9]{${APP_CONSTANTS.VALIDATION.PHONE_LENGTH}}$`)
      ]],
      direccion: ['']
    }, { validators: this.passwordMatchValidator });
  }

  private initFormPersonaJuridica(): void {
    this.registerForm = this.fb.group({
      correoElectronico: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(APP_CONSTANTS.VALIDATION.PASSWORD_MIN_LENGTH)]],
      confirmarContrasena: ['', [Validators.required]],
      razonSocial: ['', [Validators.required]],
      ruc: ['', [
        Validators.required,
        Validators.pattern(`^[0-9]{${APP_CONSTANTS.VALIDATION.RUC_LENGTH}}$`)
      ]],
      representanteLegal: [''],
      telefono: ['', [
        Validators.pattern(`^[0-9]{${APP_CONSTANTS.VALIDATION.PHONE_LENGTH}}$`)
      ]],
      direccion: ['']
    }, { validators: this.passwordMatchValidator });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.markFormGroupTouched(this.registerForm);
      return;
    }

    this.loading = true;
    const formValue = { ...this.registerForm.value };
    delete formValue.confirmarContrasena;

    const registerObservable = this.tipoCliente === 'natural'
      ? this.authService.registrarPersonaNatural(formValue)
      : this.authService.registrarPersonaJuridica(formValue);

    registerObservable.subscribe({
      next: (response) => {
        if (response.success) {
          this.notificationService.success(APP_CONSTANTS.MESSAGES.SUCCESS.REGISTER);
          this.router.navigate(['/login']);
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Error en registro:', error);
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  private passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('contrasena')?.value;
    const confirmPassword = group.get('confirmarContrasena')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // Getters para validaciones comunes
  get correo() { return this.registerForm.get('correoElectronico'); }
  get contrasena() { return this.registerForm.get('contrasena'); }
  get confirmarContrasena() { return this.registerForm.get('confirmarContrasena'); }
  get telefono() { return this.registerForm.get('telefono'); }

  // Getters para persona natural
  get nombres() { return this.registerForm.get('nombres'); }
  get apellidos() { return this.registerForm.get('apellidos'); }
  get dni() { return this.registerForm.get('dni'); }

  // Getters para persona jurídica
  get razonSocial() { return this.registerForm.get('razonSocial'); }
  get ruc() { return this.registerForm.get('ruc'); }
  get representanteLegal() { return this.registerForm.get('representanteLegal'); }
}