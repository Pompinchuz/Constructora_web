import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProformaDetailComponent } from './proforma-detail.component';

describe('ProformaDetailComponent', () => {
  let component: ProformaDetailComponent;
  let fixture: ComponentFixture<ProformaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProformaDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProformaDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
