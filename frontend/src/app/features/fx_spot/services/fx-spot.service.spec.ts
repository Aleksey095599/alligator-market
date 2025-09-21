import { TestBed } from '@angular/core/testing';

import { FxSpotService } from './fx-spot.service';

describe('FxSpotService', () => {
  let service: FxSpotService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FxSpotService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
