import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyAssociateUsersComponent } from './company-associate-users.component';

describe('CompanyAssociateUsersComponent', () => {
  let component: CompanyAssociateUsersComponent;
  let fixture: ComponentFixture<CompanyAssociateUsersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyAssociateUsersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyAssociateUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
