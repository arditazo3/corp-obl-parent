import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeTaksCollapseComponent } from './office-taks-collapse.component';

describe('OfficeTaksCollapseComponent', () => {
  let component: OfficeTaksCollapseComponent;
  let fixture: ComponentFixture<OfficeTaksCollapseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OfficeTaksCollapseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeTaksCollapseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
