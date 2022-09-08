import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderSignupComponent } from './reader-signup.component';

describe('ReaderSignupComponent', () => {
  let component: ReaderSignupComponent;
  let fixture: ComponentFixture<ReaderSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderSignupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
