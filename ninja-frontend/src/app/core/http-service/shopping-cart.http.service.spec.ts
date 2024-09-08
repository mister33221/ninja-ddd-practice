import { TestBed } from '@angular/core/testing';

import { ShoppingCartHttpService } from './shopping-cart.http.service';

describe('ShoppingCartHttpService', () => {
  let service: ShoppingCartHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShoppingCartHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
