import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductListService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  /**
   * 取得商品列表
   *
   * @returns {Observable<any>}
   */
  getProductList() {
    return this.http.get(`${this.baseUrl}/products`);
  }
}
