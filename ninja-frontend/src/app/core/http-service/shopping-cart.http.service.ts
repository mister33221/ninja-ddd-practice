import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { GetShoppingCartResponse } from 'src/app/shopping-cart/models/GetShoppingCartResponse';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartHttpService {

  constructor(private httpClient: HttpClient) {}

  /**
   * 將商品加入購物車
   */
  addToCart(productId: number): Observable<any> {
    return this.httpClient.post<any>(
      environment.BASE_URL + '/shopping-cart/add',
      {
        productId,
      }
    );
  }

  /**
   * 取得購物車(及購物車內的商品)
   */
  getShoppingCart(): Observable<GetShoppingCartResponse> {
    return this.httpClient.get<GetShoppingCartResponse>(environment.BASE_URL + '/shopping-cart');
  }
}
