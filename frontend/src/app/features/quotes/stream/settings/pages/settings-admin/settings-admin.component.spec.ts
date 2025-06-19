import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StreamConfigAdminComponent } from './settings-admin.component';

describe('StreamConfigAdminComponent', () => {
  let component: StreamConfigAdminComponent;
  let fixture: ComponentFixture<StreamConfigAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StreamConfigAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StreamConfigAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
