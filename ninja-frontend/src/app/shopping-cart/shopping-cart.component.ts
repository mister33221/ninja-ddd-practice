import { takeUntil } from 'rxjs/internal/operators/takeUntil';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { ShoppingCartHttpService } from '../core/http-service/shopping-cart.http.service';
import { GetShoppingCartResponse } from './models/GetShoppingCartResponse';
import {
  AlertService,
  AlertType,
} from '../core/components/alert/service/alert.service';

// interface CartItem {
//   id: number;
//   name: string;
//   price: number;
//   quantity: number;
//   image: string;
//   selected: boolean;
// }

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
    // this.shoppingCart = {
    //   shoppingCartId: 1,
    //   userId: 1,
    //   cartItems: [
    //     {
    //       id: 1,
    //       cartId: 1,
    //       productId: 1,
    //       productName: 'Apple',
    //       productImageURL: 'https://via.placeholder.com/150',
    //       quantity: 1,
    //       price: 10,
    //       selected: true,
    //     },
    //     {
    //       id: 2,
    //       cartId: 1,
    //       productId: 2,
    //       productName: 'Banana',
    //       productImageURL: 'https://via.placeholder.com/150',
    //       quantity: 2,
    //       price: 5,
    //       selected: true,
    //     },
    //     {
    //       id: 3,
    //       cartId: 1,
    //       productId: 3,
    //       productName: 'Orange',
    //       productImageURL: 'https://via.placeholder.com/150',
    //       quantity: 3,
    //       price: 3,
    //       selected: true,
    //     },
    //   ],
    // };
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
          // alert(JSON.stringify(shoppingCart));
          this.shoppingCart = shoppingCart;
          console.log('success');
        },
        error: (error) => {
          console.log('error');
          // alert(JSON.stringify(error));
          this.alertService.showAlert(
            AlertType.DANGER,
            '取得購物車失敗 ' + error.error.message,
            3000);
          // alert('取得購物車失敗');
        },
      });
  }

  incrementQuantity(productId: number): void {
    const foundItem = this.shoppingCart.cartItems.find(
      (i) => i.productId === productId
    );
    if (foundItem) {
      foundItem.quantity++;
    }
  }

  decrementQuantity(productId: number): void {
    const foundItem = this.shoppingCart.cartItems.find(
      (i) => i.productId === productId
    );
    if (foundItem && foundItem.quantity > 1) {
      foundItem.quantity--;
    }
  }

  removeItem(productId: number): void {
    const index = this.shoppingCart.cartItems.findIndex(
      (i) => i.productId === productId
    );
    if (index !== -1) {
      this.shoppingCart.cartItems.splice(index, 1);
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
    // const selectedItems = this.shoppingCart.cartItems.filter(
    //   (item) => item.selected
    // );
    // alert(`結帳: ${JSON.stringify(selectedItems)}`);
    // 這裡可以實現跳轉到結帳頁面或打開結帳 modal 的邏輯
  }
}
