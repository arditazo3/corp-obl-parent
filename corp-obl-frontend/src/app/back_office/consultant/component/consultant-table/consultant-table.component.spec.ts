import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultantTableComponent } from './consultant-table.component';

describe('ConsultantTableComponent', () => {
  let component: ConsultantTableComponent;
  let fixture: ComponentFixture<ConsultantTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsultantTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsultantTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
