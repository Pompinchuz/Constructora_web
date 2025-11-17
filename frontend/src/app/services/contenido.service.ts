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
  
  private apiUrl = `${environment.apiUrl}/api/contenido`;

  constructor(private http: HttpClient) {}

  // ============================================
  // GESTIÓN DE IMÁGENES
  // ============================================

  subirImagen(formData: FormData): Observable<ApiResponse<Imagen>> {
    return this.http.post<ApiResponse<Imagen>>(`${this.apiUrl}/imagenes`, formData);
  }

  obtenerImagenesPorTipo(tipo: TipoImagen): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(`${this.apiUrl}/imagenes/admin?tipo=${tipo}`);
  }

  obtenerImagenesActivasPorTipo(tipo: TipoImagen): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(`${this.apiUrl}/imagenes/publico/${tipo}`);
  }

  obtenerTodasImagenes(): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(`${this.apiUrl}/imagenes/admin`);
  }

  obtenerTodasImagenesPublicas(): Observable<ApiResponse<Imagen[]>> {
    return this.http.get<ApiResponse<Imagen[]>>(`${this.apiUrl}/imagenes/publico`);
  }

  actualizarImagen(id: number, formData: FormData): Observable<ApiResponse<Imagen>> {
    return this.http.put<ApiResponse<Imagen>>(`${this.apiUrl}/imagenes/${id}`, formData);
  }

  eliminarImagen(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/imagenes/${id}`);
  }
}


