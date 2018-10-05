import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTemplateTableComponent } from './tasktemplate-table.component';

describe('TaskTemplateTableComponent', () => {
  let component: TaskTemplateTableComponent;
  let fixture: ComponentFixture<TaskTemplateTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskTemplateTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskTemplateTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
