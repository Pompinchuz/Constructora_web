// ============================================
// AUTH.INTERCEPTOR.TS - Agregar token JWT
// ============================================

// src/app/core/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Si hay token y no es una ruta pública, agregarlo
  if (token && !isPublicRoute(req.url)) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req);
};

function isPublicRoute(url: string): boolean {
  const publicRoutes = [
    '/auth/login',
    '/auth/registro',
    '/contenido/imagenes/publico',
    '/contenido/proyectos/publico',
    '/files/public'
  ];
  
  return publicRoutes.some(route => url.includes(route));
}