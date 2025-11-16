// ============================================
// ADMIN.GUARD.TS - Solo administradores
// ============================================

// src/app/core/guards/admin.guard.ts
import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { APP_CONSTANTS } from '../../components/constants/app-constants';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.hasRole(APP_CONSTANTS.ROLES.ADMIN)) {
    return true;
  }

  // Si no es admin, redirigir a su dashboard correspondiente
  router.navigate(['/cliente/dashboard']);
  return false;
};