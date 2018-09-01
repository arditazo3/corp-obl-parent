import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyCreateEditComponent } from './company-create-edit.component';

describe('CompanyCreateEditComponent', () => {
  let component: CompanyCreateEditComponent;
  let fixture: ComponentFixture<CompanyCreateEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyCreateEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
