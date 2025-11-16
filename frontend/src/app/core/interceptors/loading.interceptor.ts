// ============================================
// LOADING.INTERCEPTOR.TS - Indicador de carga
// ============================================

// src/app/core/interceptors/loading.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../../services/loading.service';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);

  // Incrementar contador de requests activos
  loadingService.show();

  return next(req).pipe(
    finalize(() => {
      // Decrementar contador cuando termine
      loadingService.hide();
    })
  );
};