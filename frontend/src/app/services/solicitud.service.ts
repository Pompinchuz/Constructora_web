// ============================================
// SOLICITUD.SERVICE.TS
// ============================================

// src/app/services/solicitud.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../components/constants/api-endpoints';
import { ApiResponse } from '../components/models/api.models';
import { 
  SolicitudProforma, 
  EstadoSolicitud 
} from '../components/models/solicitud.models';

@Injectable({
  providedIn: 'root'
})
export class SolicitudService {
  constructor(private http: HttpClient) {}

  crearSolicitud(formData: FormData): Observable<ApiResponse<SolicitudProforma>> {
    return this.http.post<ApiResponse<SolicitudProforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.BASE}`,
      formData
    );
  }

  obtenerMisSolicitudes(): Observable<ApiResponse<SolicitudProforma[]>> {
    return this.http.get<ApiResponse<SolicitudProforma[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.MIS_SOLICITUDES}`
    );
  }

  obtenerSolicitudPorId(id: number): Observable<ApiResponse<SolicitudProforma>> {
    return this.http.get<ApiResponse<SolicitudProforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.BY_ID(id)}`
    );
  }

  obtenerTodasSolicitudes(estado?: EstadoSolicitud): Observable<ApiResponse<SolicitudProforma[]>> {
    let params = new HttpParams();
    if (estado) {
      params = params.set('estado', estado);
    }
    
    return this.http.get<ApiResponse<SolicitudProforma[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.ADMIN_TODAS}`,
      { params }
    );
  }

  aprobarSolicitud(id: number): Observable<ApiResponse<SolicitudProforma>> {
    return this.http.post<ApiResponse<SolicitudProforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.APROBAR(id)}`,
      {}
    );
  }

  rechazarSolicitud(id: number, motivo: string): Observable<ApiResponse<SolicitudProforma>> {
    const params = new HttpParams().set('motivo', motivo);
    return this.http.post<ApiResponse<SolicitudProforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.RECHAZAR(id)}`,
      {},
      { params }
    );
  }

  cambiarEstado(id: number, estado: EstadoSolicitud, motivoRechazo?: string): Observable<ApiResponse<SolicitudProforma>> {
    let params = new HttpParams().set('estado', estado);
    if (motivoRechazo) {
      params = params.set('motivoRechazo', motivoRechazo);
    }
    return this.http.patch<ApiResponse<SolicitudProforma>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.CAMBIAR_ESTADO(id)}`,
      {},
      { params }
    );
  }

  contarSolicitudesPendientes(): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${environment.apiUrl}${API_ENDPOINTS.SOLICITUDES.PENDIENTES_COUNT}`
    );
  }
}