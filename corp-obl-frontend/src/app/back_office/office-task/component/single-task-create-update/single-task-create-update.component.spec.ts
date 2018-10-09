import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleTaskCreateUpdateComponent } from './single-task-create-update.component';

describe('SingleTaskCreateUpdateComponent', () => {
  let component: SingleTaskCreateUpdateComponent;
  let fixture: ComponentFixture<SingleTaskCreateUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleTaskCreateUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleTaskCreateUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
