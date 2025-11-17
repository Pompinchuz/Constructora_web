import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadoBadgeComponent } from './estado-badge.component';

describe('EstadoBadgeComponent', () => {
  let component: EstadoBadgeComponent;
  let fixture: ComponentFixture<EstadoBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EstadoBadgeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadoBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
