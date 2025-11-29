// ============================================
// THEME.SERVICE.TS - Gestión de Tema Oscuro/Claro
// ============================================

import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';

export type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly THEME_STORAGE_KEY = 'app-theme';
  private currentTheme$ = new BehaviorSubject<Theme>('light');
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.initTheme();
  }

  /**
   * Inicializa el tema desde localStorage o preferencias del sistema
   */
  private initTheme(): void {
    if (!this.isBrowser) return;

    // Intentar cargar tema guardado
    const savedTheme = localStorage.getItem(this.THEME_STORAGE_KEY) as Theme;

    if (savedTheme) {
      this.setTheme(savedTheme);
    } else {
      // Detectar preferencia del sistema
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      this.setTheme(prefersDark ? 'dark' : 'light');
    }

    // Escuchar cambios en la preferencia del sistema
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      if (!localStorage.getItem(this.THEME_STORAGE_KEY)) {
        this.setTheme(e.matches ? 'dark' : 'light');
      }
    });
  }

  /**
   * Establece el tema y lo persiste
   */
  setTheme(theme: Theme): void {
    if (!this.isBrowser) return;

    this.currentTheme$.next(theme);
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem(this.THEME_STORAGE_KEY, theme);
  }

  /**
   * Alterna entre tema claro y oscuro
   */
  toggleTheme(): void {
    const newTheme = this.currentTheme$.value === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  /**
   * Obtiene el tema actual
   */
  getCurrentTheme(): Theme {
    return this.currentTheme$.value;
  }

  /**
   * Observable del tema actual
   */
  get theme$() {
    return this.currentTheme$.asObservable();
  }

  /**
   * Verifica si el tema actual es oscuro
   */
  isDarkMode(): boolean {
    return this.currentTheme$.value === 'dark';
  }
}
