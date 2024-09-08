import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartHttpService {
  constructor(private httpClient: HttpClient) {}

  addToCart(productId: number): Observable<any> {
    return this.httpClient.post<any>(
      environment.BASE_URL + '/shopping-cart/add',
      {
        productId,
      }
    );
  }
}
