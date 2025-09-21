import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FxSpotAdminComponent } from './fx-spot-admin.component';

describe('FxSpotAdminComponent', () => {
  let component: FxSpotAdminComponent;
  let fixture: ComponentFixture<FxSpotAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FxSpotAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FxSpotAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
