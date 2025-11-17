import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubirComprobanteComponent } from './subir-comprobante.component';

describe('SubirComprobanteComponent', () => {
  let component: SubirComprobanteComponent;
  let fixture: ComponentFixture<SubirComprobanteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubirComprobanteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubirComprobanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
