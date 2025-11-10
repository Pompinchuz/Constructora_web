import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContenidoWebComponent } from './contenido-web.component';

describe('ContenidoWebComponent', () => {
  let component: ContenidoWebComponent;
  let fixture: ComponentFixture<ContenidoWebComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContenidoWebComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContenidoWebComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
