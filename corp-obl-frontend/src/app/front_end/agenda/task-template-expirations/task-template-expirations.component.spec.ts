import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTemplateExpirationsComponent } from './task-template-expirations.component';

describe('TaskTemplateExpirationsComponent', () => {
  let component: TaskTemplateExpirationsComponent;
  let fixture: ComponentFixture<TaskTemplateExpirationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskTemplateExpirationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskTemplateExpirationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
