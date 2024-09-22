import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { CheckoutRequest } from 'src/app/shopping-cart/models/CheckoutRequest';
import {
  CartItem,
  GetShoppingCartResponse,
} from 'src/app/shopping-cart/models/GetShoppingCartResponse';
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
    return this.httpClient.get<GetShoppingCartResponse>(
      environment.BASE_URL + '/shopping-cart/get-shopping-cart'
    );
  }

  /**
   * 更新購物車內的商品
   */
  updateCartItemQuantity(cartItem: CartItem): Observable<any> {
    // 移除 selected 屬性
    const { selected, ...cartItemWithoutSelected } = cartItem;
    return this.httpClient.put<any>(
      environment.BASE_URL + '/shopping-cart/update-cart-item-quantity',
      cartItemWithoutSelected
    );
  }

  /**
   * 移除購物車內的商品
   */
  removeCartItem(cartItemId: number): Observable<any> {
    return this.httpClient.delete(
      environment.BASE_URL + '/shopping-cart/remove-cart-item/' + cartItemId
    );
  }

  /**
   * 結帳
   */
  checkout(checkoutRequest: CheckoutRequest): Observable<any> {
    return this.httpClient.post<any>(
      environment.BASE_URL + '/shopping-cart/checkout',
      checkoutRequest
    );
  }
}
