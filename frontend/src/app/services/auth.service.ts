// ============================================
// AUTH.SERVICE.TS - CON RUTAS CORRECTAS
// ============================================

import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { APP_CONSTANTS } from '../components/constants/app-constants';

// ============================================
// INTERFACES
// ============================================

export interface LoginRequest {
  correoElectronico: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  tipoToken: string;
  expiraEn: number;
  correoElectronico: string;
  tipoUsuario: string;
  nombreCompleto: string;
}

export interface RegistroPersonaNatural {
  correoElectronico: string;
  contrasena: string;
  nombres: string;
  apellidos: string;
  dni: string;
  fechaNacimiento: string;
  telefono: string;
  direccion: string;
}

export interface RegistroPersonaJuridica {
  correoElectronico: string;
  contrasena: string;
  razonSocial: string;
  ruc: string;
  representanteLegal: string;
  telefono: string;
  direccion: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private isBrowser: boolean;
  private readonly API_URL = `${environment.apiUrl}/api/auth`;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    if (this.isBrowser) {
      this.loadUserFromStorage();
    }
  }

  // ============================================
  // AUTENTICACIÓN
  // ============================================

  login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    console.log('🔐 Intentando login con:', credentials.correoElectronico);
    
    return this.http.post<ApiResponse<LoginResponse>>(
      `${this.API_URL}/login`,
      credentials
    ).pipe(
      tap(response => {
        console.log('✅ Respuesta del backend:', response);
        
        if (response.success && response.data) {
          console.log('✅ Login exitoso, guardando sesión');
          this.setSession(response.data);
        } else {
          console.error('❌ Login falló:', response.message);
        }
      }),
      catchError(error => {
        console.error('❌ Error en login:', error);
        throw error;
      })
    );
  }

  registrarPersonaNatural(data: RegistroPersonaNatural): Observable<ApiResponse<any>> {
    console.log('📝 Registrando persona natural:', data.correoElectronico);
    
    return this.http.post<ApiResponse<any>>(
      `${this.API_URL}/registro/persona-natural`,
      data
    ).pipe(
      tap(response => {
        console.log('✅ Registro exitoso:', response);
      }),
      catchError(error => {
        console.error('❌ Error en registro:', error);
        throw error;
      })
    );
  }

  registrarPersonaJuridica(data: RegistroPersonaJuridica): Observable<ApiResponse<any>> {
    console.log('📝 Registrando persona jurídica:', data.correoElectronico);
    
    return this.http.post<ApiResponse<any>>(
      `${this.API_URL}/registro/persona-juridica`,
      data
    ).pipe(
      tap(response => {
        console.log('✅ Registro exitoso:', response);
      }),
      catchError(error => {
        console.error('❌ Error en registro:', error);
        throw error;
      })
    );
  }

  logout(): void {
    console.log('🚪 Cerrando sesión');

    if (this.isBrowser) {
      sessionStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN);
      sessionStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.USER);
      sessionStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE);
    }

    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  // ============================================
  // VALIDACIONES - RUTAS CORRECTAS DEL BACKEND
  // ============================================

  checkEmailAvailability(email: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${this.API_URL}/check-email?email=${email}`
    );
  }

  checkDniAvailability(dni: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${this.API_URL}/check-dni?dni=${dni}`
    );
  }

  checkRucAvailability(ruc: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${this.API_URL}/check-ruc?ruc=${ruc}`
    );
  }

  // ============================================
  // GESTIÓN DE SESIÓN
  // ============================================

  private setSession(authResult: LoginResponse): void {
    if (!this.isBrowser) return;

    console.log('💾 Guardando sesión en sessionStorage:', authResult);

    // Usar sessionStorage en lugar de localStorage por seguridad
    sessionStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN, authResult.token);
    sessionStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE, authResult.tipoUsuario);

    const userData = {
      correo: authResult.correoElectronico,
      nombre: authResult.nombreCompleto,
      tipo: authResult.tipoUsuario
    };

    sessionStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.USER, JSON.stringify(userData));
    this.currentUserSubject.next(userData);

    console.log('✅ Sesión guardada exitosamente en sessionStorage');
  }

  private loadUserFromStorage(): void {
    if (!this.isBrowser) return;

    const userStr = sessionStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.USER);
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        this.currentUserSubject.next(user);
        console.log('✅ Usuario cargado desde sessionStorage:', user);
      } catch (e) {
        console.error('❌ Error parseando usuario:', e);
        this.logout();
      }
    }
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    return sessionStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }

  getUserType(): string | null {
    if (!this.isBrowser) return null;
    return sessionStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE);
  }

  hasRole(role: string): boolean {
    const userType = this.getUserType();
    return userType === role;
  }

  isAdmin(): boolean {
    return this.hasRole(APP_CONSTANTS.ROLES.ADMIN);
  }

  isCliente(): boolean {
    return this.hasRole(APP_CONSTANTS.ROLES.CLIENTE_NATURAL) || 
           this.hasRole(APP_CONSTANTS.ROLES.CLIENTE_JURIDICO);
  }

  isClienteNatural(): boolean {
    return this.hasRole(APP_CONSTANTS.ROLES.CLIENTE_NATURAL);
  }

  isClienteJuridico(): boolean {
    return this.hasRole(APP_CONSTANTS.ROLES.CLIENTE_JURIDICO);
  }
}