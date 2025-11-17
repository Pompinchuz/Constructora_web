// ============================================
// CLIENTE.GUARD.TS - Solo clientes
// ============================================

// src/app/core/guards/cliente.guard.ts
import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { APP_CONSTANTS } from '../../components/constants/app-constants';

export const clienteGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const hasClientRole = 
    authService.hasRole(APP_CONSTANTS.ROLES.CLIENTE_NATURAL) ||
    authService.hasRole(APP_CONSTANTS.ROLES.CLIENTE_JURIDICO);

  if (hasClientRole) {
    return true;
  }

  // Si no es cliente, redirigir a admin
  router.navigate(['/admin/dashboard']);
  return false;
};