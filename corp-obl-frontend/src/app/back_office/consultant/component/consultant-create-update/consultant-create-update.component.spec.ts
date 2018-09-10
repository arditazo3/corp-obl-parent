import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultantCreateUpdateComponent } from './consultant-create-update.component';

describe('ConsultantCreateUpdateComponent', () => {
  let component: ConsultantCreateUpdateComponent;
  let fixture: ComponentFixture<ConsultantCreateUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsultantCreateUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsultantCreateUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
