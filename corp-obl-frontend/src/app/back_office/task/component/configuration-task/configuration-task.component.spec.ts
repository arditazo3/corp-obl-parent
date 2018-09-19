import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurationTaskComponent } from './configuration-task.component';

describe('ConfigurationTaskComponent', () => {
  let component: ConfigurationTaskComponent;
  let fixture: ComponentFixture<ConfigurationTaskComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigurationTaskComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
