import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PairAdminComponent } from './pair-admin.component';

describe('PairAdminComponent', () => {
  let component: PairAdminComponent;
  let fixture: ComponentFixture<PairAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PairAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PairAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
