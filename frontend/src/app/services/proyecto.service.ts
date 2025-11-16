// ============================================
// PROYECTO.SERVICE.TS - Gestión de Proyectos Exitosos
// ============================================

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProyectoExitoso, CrearProyectoRequest } from '../components/models/contenido.models';
import { ApiResponse } from '../components/models/api.models';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {

  private baseUrl = `${environment.apiUrl}/contenido/proyectos`;

  constructor(private http: HttpClient) { }

  // ============================================
  // PROYECTOS PÚBLICOS
  // ============================================

  obtenerProyectosActivos(): Observable<ApiResponse<ProyectoExitoso[]>> {
    return this.http.get<ApiResponse<ProyectoExitoso[]>>(
      `${this.baseUrl}/publico`
    );
  }

  obtenerProyectoPorId(id: number): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.get<ApiResponse<ProyectoExitoso>>(
      `${this.baseUrl}/publico/${id}`
    );
  }

  // ============================================
  // PROYECTOS - ADMIN
  // ============================================

  obtenerTodosProyectos(): Observable<ApiResponse<ProyectoExitoso[]>> {
    return this.http.get<ApiResponse<ProyectoExitoso[]>>(
      `${this.baseUrl}/admin`
    );
  }

  crearProyecto(formData: FormData): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.post<ApiResponse<ProyectoExitoso>>(
      this.baseUrl,
      formData
    );
  }

  actualizarProyecto(id: number, proyecto: Partial<ProyectoExitoso>): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.put<ApiResponse<ProyectoExitoso>>(
      `${this.baseUrl}/${id}`,
      proyecto
    );
  }

  eliminarProyecto(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.baseUrl}/${id}`
    );
  }

  cambiarEstadoProyecto(id: number, activo: boolean): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(
      `${this.baseUrl}/${id}/activo`,
      null,
      { params: { activo: activo.toString() } }
    );
  }
}