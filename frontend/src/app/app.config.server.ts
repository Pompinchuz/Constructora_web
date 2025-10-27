import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes'; // o ./app.routes.server si lo usas
import { provideServerRendering } from '@angular/platform-server';
import { provideHttpClient } from '@angular/common/http';
import { provideNoopAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),   // 👈 imprescindible para SSR
    provideRouter(routes),
    provideHttpClient(),
    provideNoopAnimations()     // 👈 evita animaciones en render del servidor
  ]
};
