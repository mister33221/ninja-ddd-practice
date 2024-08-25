import { TestBed } from '@angular/core/testing';

import { RegistrationHttpService } from './registration.http.service';

describe('RegistrationHttpService', () => {
  let service: RegistrationHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistrationHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
