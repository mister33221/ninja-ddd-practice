import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../core/auth/auth.service';
import { AlertService } from '../core/components/alert/service/alert.service';
import { ProductHttpService } from '../core/http-service/product.http.service';
import { ProductCard } from './model/productCard';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  isLoggedIn$ = this.authService.isLoggedIn$;
  productCards: ProductCard[] = [];

  constructor(
    private readonly authService: AuthService,
    private readonly productHttpService: ProductHttpService,
    private readonly alertService: AlertService
  ) {}
  ngOnInit(): void {
    this.getProducts();
  }

  /**
   * 執行某個操作
   *
   * @description async 關鍵字用於定義一個函數是異步的。
   */
  async addToCart(productId: number): Promise<void> {
    if (await firstValueFrom(this.isLoggedIn$)) {
      const addToCartRequest: { userId: number; productId: number } = {
        userId: Number(this.authService.getAuthorizationPayloadAttr('id')),
        productId,
      };

      this.productHttpService.addToCart(addToCartRequest).subscribe({
        next: (res) => {
          this.alertService.showAlert('success', '商品已加入購物車！', 3000);
        },
        error: (err) => {
          this.alertService.showAlert(
            'danger',
            '商品加入購物車失敗！　' + err.error.message,
            3000
          );
        },
      });
    } else {
      this.showLoginModal();
    }
  }

  /**
   * 執行需要登錄的操作
   */
  showLoginModal(): void {
    const loginModalSubscription = this.authService
      .showLoginModal()
      .subscribe(() => {
        loginModalSubscription.unsubscribe();
      });
  }

  /**
   * Get the product list
   * @returns Product[]
   */
  private getProducts(): void {
    this.productHttpService
      .getProductCards()
      .subscribe({
        next: (productCards) => {
          this.productCards = productCards;
        },
        error: (error) => {
          console.error('There was an error!', error);
        },
      });
  }
}
