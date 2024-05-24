import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DependenciaComponent } from './dependencia.component';

describe('DependenciaComponent', () => {
  let component: DependenciaComponent;
  let fixture: ComponentFixture<DependenciaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DependenciaComponent]
    });
    fixture = TestBed.createComponent(DependenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
