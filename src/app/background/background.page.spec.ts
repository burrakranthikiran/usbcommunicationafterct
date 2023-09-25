import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BackgroundPage } from './background.page';

describe('BackgroundPage', () => {
  let component: BackgroundPage;
  let fixture: ComponentFixture<BackgroundPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(BackgroundPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
