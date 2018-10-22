import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpirationActivityDetailComponent } from './expiration-activity-detail.component';

describe('ExpirationActivityDetailComponent', () => {
  let component: ExpirationActivityDetailComponent;
  let fixture: ComponentFixture<ExpirationActivityDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpirationActivityDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpirationActivityDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
