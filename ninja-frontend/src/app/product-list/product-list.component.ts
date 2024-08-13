import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';
import { ProductHttpService } from '../core/http-service/prdocut/product.http.service';
import { Product } from './model/product';
import { firstValueFrom, Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit, OnDestroy  {

  products: Product[] = [];
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private productHttpService: ProductHttpService
  ) {}
  ngOnInit(): void {
    this.getProducts();
  }

  ngOnDestroy(): void {
    /**
     * 取消訂閱所有訂閱
     * 如果沒有手動取消訂閱，可能會導致記憶體洩漏
     * 造成記憶體洩漏的原因是因為訂閱者仍然保留對被觀察者的引用
     * 如果記憶體洩漏，可能會導致應用程式變慢，甚至崩潰
     * 所以在元件被銷毀時，應該取消所有訂閱
     */
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  /**
   * 執行某個操作
   *
   * @description async 關鍵字用於定義一個函數是異步的。
   */
  async onSomeAction() {
    // this.authService.isLoggedIn$.subscribe(isLoggedIn => {
    //   if (!isLoggedIn) {
    //     this.authService.showLoginModal().subscribe(() => {
    //       // 登錄模態框關閉後的操作
    //       this.performActionIfLoggedIn();
    //     });
    //   } else {
    //     this.performActionIfLoggedIn();
    //   }
    // });

    const isLoggedIn = await firstValueFrom(this.authService.isLoggedIn$);

    if (!isLoggedIn) {
      const loginModalSubscription = this.authService.showLoginModal().subscribe(() => {
        this.performActionIfLoggedIn();
        loginModalSubscription.unsubscribe();
      });
      this.subscriptions.push(loginModalSubscription);
    } else {
      this.performActionIfLoggedIn();
    }
  }

  performActionIfLoggedIn() {
    // 執行需要登錄的操作
    console.log('Performing action for logged in user');
  }

  /**
   * Get the product list
   * @returns Product[]
   */
  private getProducts(): void {
    this.productHttpService.getProducts().subscribe({
      next: products => {
        this.products = products;
      },
      error: error => {
        console.error('There was an error!', error);
      }
    });
  }
}
