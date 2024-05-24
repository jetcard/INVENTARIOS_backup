import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewComunComponent } from './new-comun.component';

describe('NewComunComponent', () => {
  let component: NewComunComponent;
  let fixture: ComponentFixture<NewComunComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewComunComponent]
    });
    fixture = TestBed.createComponent(NewComunComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
