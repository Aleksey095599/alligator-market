import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyAdminComponent } from './currency-admin.component';

describe('CurrencyAdminComponent', () => {
  let component: CurrencyAdminComponent;
  let fixture: ComponentFixture<CurrencyAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrencyAdminComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(CurrencyAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
