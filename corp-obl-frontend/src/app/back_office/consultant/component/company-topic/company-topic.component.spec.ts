import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyTopicComponent } from './company-topic.component';

describe('CompanyTopicComponent', () => {
  let component: CompanyTopicComponent;
  let fixture: ComponentFixture<CompanyTopicComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyTopicComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyTopicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
