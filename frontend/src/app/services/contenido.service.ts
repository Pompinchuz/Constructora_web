// ============================================
// CONTENIDO.SERVICE.TS - Gestión de Contenido Web
// ============================================

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Imagen, TipoImagen } from '../components/models/contenido.models';
import { ApiResponse } from '../components/models/api.models';

@Injectable({
  providedIn: 'root'
})
export class ContenidoService {

  private baseUrl = `${environment.apiUrl}/contenido`;

  constructor(private http: HttpClient) { }

  // ============================================
  // IMÁGENES PÚBLICAS
  // ============================================

  obtenerImagenesPorTipo(tipo: TipoImagen): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(
      `${this.baseUrl}/imagenes/publico/${tipo}`
    );
  }

  obtenerTodasImagenesPublicas(): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(
      `${this.baseUrl}/imagenes/publico`
    );
  }

  // ============================================
  // IMÁGENES - ADMIN
  // ============================================

  obtenerTodasImagenes(): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(
      `${this.baseUrl}/imagenes/admin`
    );
  }

  subirImagen(formData: FormData): Observable<ApiResponse<Imagen>> {
    return this.http.post<ApiResponse<Imagen>>(
      `${this.baseUrl}/imagenes`,
      formData
    );
  }

  actualizarImagen(id: number, formData: FormData): Observable<ApiResponse<Imagen>> {
    return this.http.put<ApiResponse<Imagen>>(
      `${this.baseUrl}/imagenes/${id}`,
      formData
    );
  }

  eliminarImagen(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.baseUrl}/imagenes/${id}`
    );
  }

  cambiarEstadoImagen(id: number, activo: boolean): Observable<ApiResponse<Imagen>> {
    return this.http.put<ApiResponse<Imagen>>(
      `${this.baseUrl}/imagenes/${id}`,
      null,
      { params: { activo: activo.toString() } }
    );
  }
}