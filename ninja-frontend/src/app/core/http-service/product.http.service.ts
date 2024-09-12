import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProductCard } from 'src/app/product-list/model/productCard';

@Injectable({
  providedIn: 'root',
})
export class ProductHttpService {
  constructor(private http: HttpClient) {}

  /**
   * Get the product list
   * @returns Product[]
   */
  getProductCards(): Observable<ProductCard[]> {
    return this.http.get<ProductCard[]>(
      environment.BASE_URL + '/product/get-product-cards'
    );
  }

  addToCart(addToCartRequest: {
    userId: number;
    productId: number;
  }): Observable<any> {
    return this.http.post<any>(environment.BASE_URL + '/shopping-cart/add-to-cart', {
      userId: addToCartRequest.userId,
      productId: addToCartRequest.productId,
    });
  }
}
