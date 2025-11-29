// ============================================
// APP.ROUTES.TS - Rutas Actualizadas con Sección Pública
// ============================================

import { Routes } from '@angular/router';
import { HomeComponent } from './components/public/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ClienteDashboardComponent } from './components/cliente/cliente-dashboard/cliente-dashboard.component';
import { AprobacionProyectosComponent } from './components/cliente/aprobacion-proyectos/aprobacion-proyectos.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },

  // Rutas de Cliente
  { path: 'cliente/dashboard', component: ClienteDashboardComponent },
  { path: 'cliente/aprobacion-proyectos', component: AprobacionProyectosComponent },

];
