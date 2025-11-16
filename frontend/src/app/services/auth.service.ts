// ============================================
// AUTH.SERVICE.TS - CORREGIDO PARA SSR
// ============================================

// src/app/services/auth.service.ts
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../components/constants/api-endpoints';
import { APP_CONSTANTS } from '../components/constants/app-constants';
import { 
  LoginRequest, 
  LoginResponse, 
  RegistroPersonaNatural, 
  RegistroPersonaJuridica 
} from '../components/models/auth.models';
import { ApiResponse } from '../components/models/api.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    // Solo cargar del storage si estamos en el navegador
    if (this.isBrowser) {
      this.loadUserFromStorage();
    }
  }

  // ============================================
  // AUTENTICACIÓN
  // ============================================

  login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.LOGIN}`,
      credentials
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          this.setSession(response.data);
        }
      })
    );
  }

  registrarPersonaNatural(data: RegistroPersonaNatural): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.REGISTER_NATURAL}`,
      data
    );
  }

  registrarPersonaJuridica(data: RegistroPersonaJuridica): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.REGISTER_JURIDICO}`,
      data
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN);
      localStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.USER);
      localStorage.removeItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE);
    }
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  // ============================================
  // VALIDACIONES
  // ============================================

  checkEmailAvailability(email: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.CHECK_EMAIL}?email=${email}`
    );
  }

  checkDniAvailability(dni: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.CHECK_DNI}?dni=${dni}`
    );
  }

  checkRucAvailability(ruc: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(
      `${environment.apiUrl}${API_ENDPOINTS.AUTH.CHECK_RUC}?ruc=${ruc}`
    );
  }

  // ============================================
  // GESTIÓN DE SESIÓN
  // ============================================

  private setSession(authResult: LoginResponse): void {
    if (!this.isBrowser) return;

    localStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN, authResult.token);
    localStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE, authResult.tipoUsuario);
    
    const userData = {
      correo: authResult.correoElectronico,
      nombre: authResult.nombreCompleto,
      tipo: authResult.tipoUsuario
    };
    
    localStorage.setItem(APP_CONSTANTS.STORAGE_KEYS.USER, JSON.stringify(userData));
    this.currentUserSubject.next(userData);
  }

  private loadUserFromStorage(): void {
    if (!this.isBrowser) return;

    const userStr = localStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.USER);
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        this.currentUserSubject.next(user);
      } catch (e) {
        this.logout();
      }
    }
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.TOKEN);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }

  getUserType(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(APP_CONSTANTS.STORAGE_KEYS.USER_TYPE);
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
}