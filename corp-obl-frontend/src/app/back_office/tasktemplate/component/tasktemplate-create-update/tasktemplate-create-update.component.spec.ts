import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTemplateCreateUpdateComponent } from './tasktemplate-create-update.component';

describe('TaskTemplateCreateUpdateComponent', () => {
  let component: TaskTemplateCreateUpdateComponent;
  let fixture: ComponentFixture<TaskTemplateCreateUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskTemplateCreateUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskTemplateCreateUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
