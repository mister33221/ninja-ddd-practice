import { TestBed } from '@angular/core/testing';

import { LoginModalService } from './login-modal.service';

describe('LoginModalService', () => {
  let service: LoginModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
