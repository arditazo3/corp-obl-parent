import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeCreateEditComponent } from './office-create-edit.component';

describe('OfficeCreateEditComponent', () => {
  let component: OfficeCreateEditComponent;
  let fixture: ComponentFixture<OfficeCreateEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OfficeCreateEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
