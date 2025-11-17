import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisProformasComponent } from './mis-proformas.component';

describe('MisProformasComponent', () => {
  let component: MisProformasComponent;
  let fixture: ComponentFixture<MisProformasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisProformasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisProformasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
