import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssociationOfficeComponent } from './association-office.component';

describe('AssociationOfficeComponent', () => {
  let component: AssociationOfficeComponent;
  let fixture: ComponentFixture<AssociationOfficeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssociationOfficeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssociationOfficeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
