import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config.server';

// Angular SSR inyecta el contexto como primer parámetro.
// NO lo envuelvas en { context } y NO intentes importar el tipo.
export default function bootstrap(context: unknown) {
  return bootstrapApplication(AppComponent, appConfig, context as any);
}
