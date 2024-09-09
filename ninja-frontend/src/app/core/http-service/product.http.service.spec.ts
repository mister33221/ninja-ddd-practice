import { TestBed } from '@angular/core/testing';

import { ProductHttpService } from './product.http.service';

describe('ProductHttpService', () => {
  let service: ProductHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
