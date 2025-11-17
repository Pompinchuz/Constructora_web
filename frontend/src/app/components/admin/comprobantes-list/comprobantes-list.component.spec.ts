import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComprobantesListComponent } from './comprobantes-list.component';

describe('ComprobantesListComponent', () => {
  let component: ComprobantesListComponent;
  let fixture: ComponentFixture<ComprobantesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComprobantesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComprobantesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
