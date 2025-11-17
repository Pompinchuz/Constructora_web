// ============================================
// APP.COMPONENT.TS - CORREGIDO (Sin error NG0100)
// ============================================

import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { filter, Observable } from 'rxjs';
import { AuthService } from './services/auth.service';
import { LoadingService } from './services/loading.service';
import { NotificationService, Notification } from './services/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  loading$!: Observable<boolean>;
  notification: Notification | null = null;
  isAuthenticated = false;
  userName = '';
  private notificationTimeout: any;
  private isBrowser: boolean;

  constructor(
    private authService: AuthService,
    private loadingService: LoadingService,
    private notificationService: NotificationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    // Inicializar loading$
    this.loading$ = this.loadingService.loading$;

    // Suscribirse a cambios de autenticación
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = !!user;
      this.userName = user?.nombre || '';
      // Forzar detección de cambios
      this.cdr.detectChanges();
    });

    // Suscribirse a notificaciones
    this.notificationService.notification$.subscribe(notification => {
      this.notification = notification;
      
      // Auto-cerrar después del tiempo especificado
      if (this.notificationTimeout) {
        clearTimeout(this.notificationTimeout);
      }
      
      this.notificationTimeout = setTimeout(() => {
        this.closeNotification();
      }, notification.duration || 3000);
    });

    // Scroll to top en cada navegación (solo en el navegador)
    if (this.isBrowser) {
      this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        window.scrollTo(0, 0);
      });
    }
  }

  closeNotification(): void {
    this.notification = null;
    if (this.notificationTimeout) {
      clearTimeout(this.notificationTimeout);
    }
  }

  logout(): void {
    this.authService.logout();
  }
}