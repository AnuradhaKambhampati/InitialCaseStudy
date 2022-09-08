import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderSigninComponent } from './reader-signin.component';

describe('ReaderSigninComponent', () => {
  let component: ReaderSigninComponent;
  let fixture: ComponentFixture<ReaderSigninComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderSigninComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderSigninComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
