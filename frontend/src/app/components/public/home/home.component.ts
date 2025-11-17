// ============================================
// HOME.COMPONENT.TS - Página Principal Pública
// ============================================

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { HeroSectionComponent } from '../hero-section/hero-section.component';
import { ServiciosComponent } from '../servicios/servicios.component';
import { ProyectosComponent } from '../proyectos/proyectos.component';
import { SobreNosotrosComponent } from '../sobre-nosotros/sobre-nosotros.component';
import { ContactoComponent } from '../contacto/contacto.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    HeroSectionComponent,
    ServiciosComponent,
    ProyectosComponent,
    SobreNosotrosComponent,
    ContactoComponent,
    FooterComponent
  ],
  template: `
    <div class="min-h-screen flex flex-col">
      <app-navbar></app-navbar>
      <main class="flex-grow">
        <section id="inicio">
          <app-hero-section></app-hero-section>
        </section>

        <section id="servicios">
          <app-servicios></app-servicios>
        </section>

        <section id="proyectos">
          <app-proyectos></app-proyectos>
        </section>

        <section id="sobre-nosotros">
          <app-sobre-nosotros></app-sobre-nosotros>
        </section>

        <section id="contacto">
          <app-contacto></app-contacto>
        </section>
      </main>
      <app-footer></app-footer>
    </div>
  `
})
export class HomeComponent { }
