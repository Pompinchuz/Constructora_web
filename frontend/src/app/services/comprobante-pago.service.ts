import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../components/constants/api-endpoints';
import { ApiResponse } from '../components/models/api.models';

export interface ComprobantePago {
  id: number;
  proformaId: number;
  proformaCodigo: string;
  clienteNombre: string;
  monto: number;
  numeroOperacion?: string;
  entidadBancaria?: string;
  archivoComprobante: string;
  estado: EstadoComprobante;
  observaciones?: string;
  fechaSubida: Date;
  verificadoPor?: string;
  fechaVerificacion?: Date;
}

export enum EstadoComprobante {
  PENDIENTE = 'PENDIENTE',
  VERIFICADO = 'VERIFICADO',
  RECHAZADO = 'RECHAZADO'
}

@Injectable({
  providedIn: 'root'
})
export class ComprobantePagoService {

  constructor(private http: HttpClient) {}

  subirComprobante(
    proformaId: number,
    monto: number,
    archivo: File,
    numeroOperacion?: string,
    entidadBancaria?: string,
    observaciones?: string
  ): Observable<ApiResponse<ComprobantePago>> {
    const formData = new FormData();
    formData.append('proformaId', proformaId.toString());
    formData.append('monto', monto.toString());
    formData.append('archivo', archivo);

    if (numeroOperacion) {
      formData.append('numeroOperacion', numeroOperacion);
    }
    if (entidadBancaria) {
      formData.append('entidadBancaria', entidadBancaria);
    }
    if (observaciones) {
      formData.append('observaciones', observaciones);
    }

    return this.http.post<ApiResponse<ComprobantePago>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.BASE}`,
      formData
    );
  }

  obtenerComprobantePorId(id: number): Observable<ApiResponse<ComprobantePago>> {
    return this.http.get<ApiResponse<ComprobantePago>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.BY_ID(id)}`
    );
  }

  obtenerMisComprobantes(): Observable<ApiResponse<ComprobantePago[]>> {
    return this.http.get<ApiResponse<ComprobantePago[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.MIS_COMPROBANTES}`
    );
  }

  obtenerComprobantesPorProforma(proformaId: number): Observable<ApiResponse<ComprobantePago[]>> {
    return this.http.get<ApiResponse<ComprobantePago[]>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.BY_PROFORMA(proformaId)}`
    );
  }

  // Métodos para Admin (futuro uso)
  verificarComprobante(id: number): Observable<ApiResponse<ComprobantePago>> {
    return this.http.post<ApiResponse<ComprobantePago>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.VERIFICAR(id)}`,
      {}
    );
  }

  rechazarComprobante(id: number, motivo: string): Observable<ApiResponse<ComprobantePago>> {
    return this.http.post<ApiResponse<ComprobantePago>>(
      `${environment.apiUrl}${API_ENDPOINTS.COMPROBANTES.RECHAZAR(id)}`,
      null,
      { params: { motivo } }
    );
  }
}
