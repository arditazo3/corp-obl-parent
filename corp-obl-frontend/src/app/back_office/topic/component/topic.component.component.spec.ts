import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Topic.ComponentComponent } from './component.component';

describe('Topic.ComponentComponent', () => {
  let component: Topic.ComponentComponent;
  let fixture: ComponentFixture<Topic.ComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Topic.ComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Topic.ComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
