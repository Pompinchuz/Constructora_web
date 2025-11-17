import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearProformaComponent } from './crear-proforma.component';

describe('CrearProformaComponent', () => {
  let component: CrearProformaComponent;
  let fixture: ComponentFixture<CrearProformaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearProformaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearProformaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
