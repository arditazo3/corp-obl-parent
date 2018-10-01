import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuickConfigurationComponent } from './quick-configuration.component';

describe('QuickConfigurationComponent', () => {
  let component: QuickConfigurationComponent;
  let fixture: ComponentFixture<QuickConfigurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuickConfigurationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuickConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
