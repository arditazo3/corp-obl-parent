import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeExpirationActivityComponent } from './office-expiration-activity.component';

describe('OfficeExpirationActivityComponent', () => {
  let component: OfficeExpirationActivityComponent;
  let fixture: ComponentFixture<OfficeExpirationActivityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OfficeExpirationActivityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeExpirationActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
