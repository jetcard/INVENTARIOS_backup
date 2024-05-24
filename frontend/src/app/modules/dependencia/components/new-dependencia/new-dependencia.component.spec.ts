import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDependenciaComponent } from './new-dependencia.component';

describe('NewDependenciaComponent', () => {
  let component: NewDependenciaComponent;
  let fixture: ComponentFixture<NewDependenciaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewDependenciaComponent]
    });
    fixture = TestBed.createComponent(NewDependenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
