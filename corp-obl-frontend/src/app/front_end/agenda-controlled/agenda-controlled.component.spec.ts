import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgendaControlledComponent } from './agenda-controlled.component';

describe('AgendaControlledComponent', () => {
  let component: AgendaControlledComponent;
  let fixture: ComponentFixture<AgendaControlledComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgendaControlledComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgendaControlledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
