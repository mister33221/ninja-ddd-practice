import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';
import {
  AlertService,
  AlertType
} from '../core/components/alert/service/alert.service';
import { ShoppingCartHttpService } from '../core/http-service/shopping-cart.http.service';
import { GetShoppingCartResponse } from './models/GetShoppingCartResponse';
import { CheckoutRequest } from './models/CheckoutRequest';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss'],
})
export class ShoppingCartComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  shoppingCart = {} as GetShoppingCartResponse;

  constructor(
    private shoppingCartHttpService: ShoppingCartHttpService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.getShoppingCart();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getShoppingCart(): void {
    this.shoppingCartHttpService
      .getShoppingCart()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (shoppingCart: GetShoppingCartResponse) => {
          this.shoppingCart = shoppingCart;
          console.log('success');
        },
        error: (error) => {
          console.log('error');
          this.alertService.showAlert(
            AlertType.DANGER,
            '取得購物車失敗 ' + error.error.message,
            3000);
        },
      });
  }

  incrementQuantity(productId: number): void {
    const foundItem = this.shoppingCart.cartItems.find(
      (i) => i.productId === productId
    );
    if (foundItem) {
      foundItem.quantity++;
      this.shoppingCartHttpService.updateCartItemQuantity(foundItem).subscribe({
        next: () => {
          this.alertService.showAlert(
            AlertType.SUCCESS,
            '更新購物車成功',
            3000);
        }
      });
    }
  }

  decrementQuantity(productId: number): void {
    const foundItem = this.shoppingCart.cartItems.find(
      (i) => i.productId === productId
    );
    if (foundItem && foundItem.quantity > 1) {
      foundItem.quantity--;
      this.shoppingCartHttpService.updateCartItemQuantity(foundItem).subscribe({
        next: () => {
          this.alertService.showAlert(
            AlertType.SUCCESS,
            '更新購物車成功',
            3000);
        }
      });
    }
  }

  removeItem(cartItemId: number): void {
    const index = this.shoppingCart.cartItems.findIndex(
      (i) => i.id === cartItemId
    );
    // 如果找不到該商品，就會回傳 -1，所以是如果有找到，就執行以下內容，沒找到就不執行
    if (index !== -1) {
      this.shoppingCartHttpService.removeCartItem(cartItemId).subscribe({
        next: () => {
          this.shoppingCart.cartItems.splice(index, 1);
          this.alertService.showAlert(
            AlertType.SUCCESS,
            '移除商品成功',
            3000);
        }
      });
    }
  }

  getTotalPrice(): number {
    if (!this.shoppingCart.cartItems) {
      return 0;
    }
    return this.shoppingCart.cartItems
      .filter((item) => item.selected)
      .reduce((acc, item) => acc + item.price * item.quantity, 0);
  }

  checkout(): void {
    const shoppingCartWithSelectedItems: CheckoutRequest = {
      shoppingCartId: this.shoppingCart.shoppingCartId,
      userId: this.shoppingCart.userId,
      cartItems: this.shoppingCart.cartItems.filter((item) => item.selected),
    };

    if (shoppingCartWithSelectedItems.cartItems.length === 0) {
      this.alertService.showAlert(
        AlertType.WARNING,
        '請選擇商品',
        3000);
      return;
    }

    this.shoppingCartHttpService.checkout(shoppingCartWithSelectedItems).subscribe({
      next: () => {
        this.shoppingCart.cartItems = this.shoppingCart.cartItems.filter(
          (item) => !item.selected
        );
        this.alertService.showAlert(
          AlertType.SUCCESS,
          '結帳成功',
          3000);
      }
    });
  }

  isEmptyCart(): boolean {
    return this.shoppingCart.cartItems?.length === 0;
  }
}
