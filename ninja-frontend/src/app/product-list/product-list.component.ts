import { ShoppingCartHttpService } from './../core/http-service/shopping-cart.http.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';
import { ProductHttpService } from '../core/http-service/prdocut/product.http.service';
import {
  firstValueFrom,
  Observable,
  Subject,
  Subscription,
  take,
  takeUntil,
} from 'rxjs';
import { ProductCard } from './model/productCard';
import { AlertService } from '../core/components/alert/service/alert.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  isLoggedIn$ = this.authService.isLoggedIn$;
  productCards: ProductCard[] = [];

  constructor(
    private authService: AuthService,
    private productHttpService: ProductHttpService,
    private alertService: AlertService,
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
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * 執行某個操作
   *
   * @description async 關鍵字用於定義一個函數是異步的。
   */
  async addToCart(productId: number): Promise<void> {
    if (await firstValueFrom(this.isLoggedIn$)) {
      const addToCartRequest: { userId: number; productId: number } = {
        userId: Number(this.authService.getJwtPayloadAttr('id')),
        productId,
      };

      this.productHttpService.addToCart(addToCartRequest)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (res) => {
            this.alertService.showAlert(
              'success',
              '商品已加入購物車！',
              3000
            );
          },
          error: (err) => {
            this.alertService.showAlert('danger',
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
      .pipe(takeUntil(this.destroy$))
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
      .pipe(takeUntil(this.destroy$))
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
