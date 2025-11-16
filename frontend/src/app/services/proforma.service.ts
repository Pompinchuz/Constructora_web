// ============================================
// PROFORMA.SERVICE.TS
// ============================================

// src/app/services/proforma.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../components/constants/api-endpoints';
import { ApiResponse } from '../components/models/api.models';
import { 
  Proforma, 
  CrearProformaRequest, 
  EstadoProforma 
} from '../components/models/proforma.models';

@Injectable({
  providedIn: 'root'
})
export class ProformaService {
  constructor(private http: HttpClient) {}

  crearProforma(data: CrearProformaRequest): Observable<ApiResponse<Proforma>> {
    return this.http.post<ApiResponse<Proforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.BASE}`,
      data
    );
  }

  enviarProforma(id: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.ENVIAR(id)}`,
      {}
    );
  }

  obtenerProformaPorId(id: number): Observable<ApiResponse<Proforma>> {
    return this.http.get<ApiResponse<Proforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.BY_ID(id)}`
    );
  }

  obtenerProformaPorCodigo(codigo: string): Observable<ApiResponse<Proforma>> {
    return this.http.get<ApiResponse<Proforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.BY_CODIGO(codigo)}`
    );
  }

  obtenerMisProformas(): Observable<ApiResponse<Proforma[]>> {
    return this.http.get<ApiResponse<Proforma[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.MIS_PROFORMAS}`
    );
  }

  obtenerTodasProformas(estado?: EstadoProforma): Observable<ApiResponse<Proforma[]>> {
    let params = new HttpParams();
    if (estado) {
      params = params.set('estado', estado);
    }
    
    return this.http.get<ApiResponse<Proforma[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.ADMIN_TODAS}`,
      { params }
    );
  }

  marcarComoVista(id: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.MARCAR_VISTA(id)}`,
      {}
    );
  }

  obtenerEstadisticas(): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(
      `${environment.apiUrl}${API_ENDPOINTS.PROFORMAS.ESTADISTICAS}`
    );
  }
}