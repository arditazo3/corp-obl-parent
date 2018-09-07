import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicCreateUpdateComponent } from './topic-create-update.component';

describe('TopicCreateUpdateComponent', () => {
  let component: TopicCreateUpdateComponent;
  let fixture: ComponentFixture<TopicCreateUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopicCreateUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicCreateUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
