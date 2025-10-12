// app.routes.ts
import { Routes } from '@angular/router';
import { ClienteFormComponent } from './components/cliente-form/cliente-form.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
  { path: '', component: ClienteFormComponent },
  { path: 'login', component: LoginComponent },
];
