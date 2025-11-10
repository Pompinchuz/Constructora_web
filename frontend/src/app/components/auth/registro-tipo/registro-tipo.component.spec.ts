import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroTipoComponent } from './registro-tipo.component';

describe('RegistroTipoComponent', () => {
  let component: RegistroTipoComponent;
  let fixture: ComponentFixture<RegistroTipoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroTipoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistroTipoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
