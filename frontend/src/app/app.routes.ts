// ============================================
// APP.ROUTES.TS - Rutas Actualizadas con Sección Pública
// ============================================

import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { clienteGuard } from './core/guards/cliente.guard';

// Public Layout
import { PublicLayoutComponent } from './components/public/public-layout/public-layout.component';
import { HomeComponent } from './components/public/home/home.component';

// Auth Components
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { RegistroTipoComponent } from './components/auth/registro-tipo/registro-tipo.component';

// Admin Components
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { SolicitudesListComponent } from './components/admin/solicitudes-list/solicitudes-list.component';

// Cliente Components
import { ClienteDashboardComponent } from './components/cliente/cliente-dashboard/cliente-dashboard.component';
import { NuevaSolicitudComponent } from './components/cliente/nueva-solicitud/nueva-solicitud.component';
import { MisSolicitudesComponent } from './components/cliente/mis-solicitudes/mis-solicitudes.component';

export const routes: Routes = [
  // ==========================================
  // RUTAS PÚBLICAS (Landing Page)
  // ==========================================
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      {
        path: '',
        component: HomeComponent,
        title: 'Inicio - Constructora'
      }
    ]
  },

  // ==========================================
  // RUTAS DE AUTENTICACIÓN
  // ==========================================
  {
    path: 'login',
    component: LoginComponent,
    title: 'Iniciar Sesión'
  },
  {
    path: 'registro-tipo',
    component: RegistroTipoComponent,
    title: 'Seleccionar Tipo de Registro'
  },
  {
    path: 'register/:tipo',
    component: RegisterComponent,
    title: 'Registrarse'
  },

  // ==========================================
  // RUTAS DE ADMINISTRADOR
  // ==========================================
  {
    path: 'admin',
    canActivate: [authGuard, adminGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: AdminDashboardComponent,
        title: 'Dashboard - Admin'
      },
      {
        path: 'solicitudes',
        component: SolicitudesListComponent,
        title: 'Gestión de Solicitudes'
      },
      {
        path: 'solicitudes/:id',
        loadComponent: () => import('./components/admin/solicitud-detail/solicitud-detail.component')
          .then(m => m.SolicitudDetailComponent),
        title: 'Detalle de Solicitud'
      },
      {
        path: 'crear-proforma/:solicitudId',
        loadComponent: () => import('./components/admin/crear-proforma/crear-proforma.component')
          .then(m => m.CrearProformaComponent),
        title: 'Crear Proforma'
      },
      {
        path: 'proformas',
        loadComponent: () => import('./components/admin/proformas-list/proformas-list.component')
          .then(m => m.ProformasListComponent),
        title: 'Gestión de Proformas'
      },
      {
        path: 'comprobantes',
        loadComponent: () => import('./components/admin/comprobantes-list/comprobantes-list.component')
          .then(m => m.ComprobantesListComponent),
        title: 'Gestión de Comprobantes'
      },
      {
        path: 'contenido',
        loadComponent: () => import('./components/admin/contenido-web/contenido-web.component')
          .then(m => m.ContenidoWebComponent),
        title: 'Gestión de Contenido'
      },
      {
        path: 'proyectos',
        loadComponent: () => import('./components/admin/proyectos-list/proyectos-list.component')
          .then(m => m.ProyectosListComponent),
        title: 'Gestión de Proyectos'
      },
      {
        path: 'crear-proyecto',
        loadComponent: () => import('./components/admin/crear-proyecto/crear-proyecto.component')
          .then(m => m.CrearProyectoComponent),
        title: 'Crear Proyecto'
      }
    ]
  },

  // ==========================================
  // RUTAS DE CLIENTE
  // ==========================================
  {
    path: 'cliente',
    canActivate: [authGuard, clienteGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: ClienteDashboardComponent,
        title: 'Mi Dashboard'
      },
      {
        path: 'nueva-solicitud',
        component: NuevaSolicitudComponent,
        title: 'Nueva Solicitud'
      },
      {
        path: 'mis-solicitudes',
        component: MisSolicitudesComponent,
        title: 'Mis Solicitudes'
      },
      {
        path: 'mis-proformas',
        loadComponent: () => import('./components/cliente/mis-proformas/mis-proformas.component')
          .then(m => m.MisProformasComponent),
        title: 'Mis Proformas'
      },
      {
        path: 'proforma/:id',
        loadComponent: () => import('./components/cliente/proforma-detail/proforma-detail.component')
          .then(m => m.ProformaDetailComponent),
        title: 'Detalle de Proforma'
      },
      {
        path: 'subir-comprobante/:proformaId',
        loadComponent: () => import('./components/cliente/subir-comprobante/subir-comprobante.component')
          .then(m => m.SubirComprobanteComponent),
        title: 'Subir Comprobante'
      }
    ]
  },

  // ==========================================
  // RUTA 404 - No encontrado
  // ==========================================
  {
    path: '**',
    redirectTo: ''
  }
];