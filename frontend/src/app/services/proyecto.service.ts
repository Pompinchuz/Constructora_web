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
  
  private apiUrl = `${environment.apiUrl}/api/contenido`;

  constructor(private http: HttpClient) {}

  // ============================================
  // GESTIÓN DE PROYECTOS
  // ============================================

  crearProyecto(formData: FormData): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.post<ApiResponse<ProyectoExitoso>>(`${this.apiUrl}/proyectos`, formData);
  }

  obtenerProyectoPorId(id: number): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.get<ApiResponse<ProyectoExitoso>>(`${this.apiUrl}/proyectos/publico/${id}`);
  }

  obtenerTodosProyectos(): Observable<ApiResponse<ProyectoExitoso[]>> {
    return this.http.get<ApiResponse<ProyectoExitoso[]>>(`${this.apiUrl}/proyectos/admin`);
  }

  obtenerProyectosActivos(): Observable<ApiResponse<ProyectoExitoso[]>> {
    return this.http.get<ApiResponse<ProyectoExitoso[]>>(`${this.apiUrl}/proyectos/publico`);
  }

  actualizarProyecto(id: number, datos: any): Observable<ApiResponse<ProyectoExitoso>> {
    return this.http.put<ApiResponse<ProyectoExitoso>>(`${this.apiUrl}/proyectos/${id}`, datos);
  }

  cambiarEstadoProyecto(id: number, activo: boolean): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(`${this.apiUrl}/proyectos/${id}/activo?activo=${activo}`, {});
  }

  eliminarProyecto(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/proyectos/${id}`);
  }
}




























