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
    return this.http.get<ProductCard[]>(environment.BASE_URL + '/product/get-product-cards');
    // 先回傳一個假資料的 Observable
    // return new Observable<Product[]>((observer) => {
    //   observer.next([
    //     {
    //       id: 1,
    //       name: 'Product 1',
    //       description: 'This is product 1',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 100,
    //       category: 'Category 1',
    //     },
    //     {
    //       id: 2,
    //       name: 'Product 2',
    //       description: 'This is product 2',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 200,
    //       category: 'Category 2',
    //     },
    //     {
    //       id: 3,
    //       name: 'Product 3',
    //       description: 'This is product 3',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 300,
    //       category: 'Category 3',
    //     },
    //     {
    //       id: 4,
    //       name: 'Product 4',
    //       description: 'This is product 4',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 400,
    //       category: 'Category 4',
    //     },
    //     {
    //       id: 5,
    //       name: 'Product 5',
    //       description: 'This is product 5',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 500,
    //       category: 'Category 5',
    //     },
    //     {
    //       id: 6,
    //       name: 'Product 6',
    //       description: 'This is product 6',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 600,
    //       category: 'Category 6',
    //     },
    //     {
    //       id: 7,
    //       name: 'Product 7',
    //       description: 'This is product 7',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 700,
    //       category: 'Category 7',
    //     },
    //     {
    //       id: 8,
    //       name: 'Product 8',
    //       description: 'This is product 8',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 800,
    //       category: 'Category 8',
    //     },
    //     {
    //       id: 9,
    //       name: 'Product 9',
    //       description: 'This is product 9',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 900,
    //       category: 'Category 9',
    //     },
    //     {
    //       id: 10,
    //       name: 'Product 10',
    //       description: 'This is product 10',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 1000,
    //       category: 'Category 10',
    //     },
    //     {
    //       id: 11,
    //       name: 'Product 11',
    //       description: 'This is product 11',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 1100,
    //       category: 'Category 11',
    //     },
    //     {
    //       id: 12,
    //       name: 'Product 12',
    //       description: 'This is product 12',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 1200,
    //       category: 'Category 12',
    //     },
    //     {
    //       id: 13,
    //       name: 'Product 13',
    //       description: 'This is product 13',
    //       imageUrl: 'https://via.placeholder.com/150',
    //       price: 1300,
    //       category: 'Category 13',
    //     },
    //   ]);
    //   observer.complete();
    // });
  }
}
