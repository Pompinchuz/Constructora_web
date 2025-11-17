// ============================================
// ERROR.INTERCEPTOR.TS - Manejo de errores
// ============================================

// src/app/core/interceptors/error.interceptor.ts
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { APP_CONSTANTS } from '../../components/constants/app-constants';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = APP_CONSTANTS.MESSAGES.ERROR.GENERIC;

      if (error.error instanceof ErrorEvent) {
        // Error del cliente
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // Error del servidor
        switch (error.status) {
          case 0:
            errorMessage = APP_CONSTANTS.MESSAGES.ERROR.NETWORK;
            break;
          case 401:
            errorMessage = APP_CONSTANTS.MESSAGES.ERROR.SESSION_EXPIRED;
            authService.logout();
            router.navigate(['/login']);
            break;
          case 403:
            errorMessage = APP_CONSTANTS.MESSAGES.ERROR.UNAUTHORIZED;
            break;
          case 404:
            errorMessage = 'Recurso no encontrado';
            break;
          case 500:
            errorMessage = 'Error interno del servidor';
            break;
          default:
            if (error.error?.message) {
              errorMessage = error.error.message;
            }
        }
      }

      // Mostrar notificación solo si no es 401 (ya se maneja en AuthService)
      if (error.status !== 401) {
        notificationService.error(errorMessage);
      }

      return throwError(() => error);
    })
  );
};