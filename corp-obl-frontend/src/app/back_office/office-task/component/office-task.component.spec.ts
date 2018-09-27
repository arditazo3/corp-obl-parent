import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeTaskComponent } from './office-task.component';

describe('OfficeTaskComponent', () => {
  let component: OfficeTaskComponent;
  let fixture: ComponentFixture<OfficeTaskComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OfficeTaskComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
