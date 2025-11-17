import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProformasListComponent } from './proformas-list.component';

describe('ProformasListComponent', () => {
  let component: ProformasListComponent;
  let fixture: ComponentFixture<ProformasListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProformasListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProformasListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
