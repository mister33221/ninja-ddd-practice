import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';

interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  selected: boolean;
}

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent  implements OnInit {
  cartItems: CartItem[] = [
    { id: 1, name: '商品 1', price: 100, quantity: 1, image: 'https://picsum.photos/200/200?random=1', selected: true },
    { id: 2, name: '商品 2', price: 200, quantity: 2, image: 'https://picsum.photos/200/200?random=2', selected: true },
    { id: 3, name: '商品 3', price: 150, quantity: 1, image: 'https://picsum.photos/200/200?random=3', selected: true },
  ];

  constructor(private modalService: BsModalService) {}

  ngOnInit(): void {}

  incrementQuantity(item: CartItem): void {
    item.quantity++;
  }

  decrementQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      item.quantity--;
    }
  }

  removeItem(item: CartItem): void {
    this.cartItems = this.cartItems.filter(i => i.id !== item.id);
  }

  getTotalPrice(): number {
    return this.cartItems
      .filter(item => item.selected)
      .reduce((total, item) => total + item.price * item.quantity, 0);
  }

  checkout(): void {
    const selectedItems = this.cartItems.filter(item => item.selected);
    console.log('Checkout with items:', selectedItems);
    // 這裡可以實現跳轉到結帳頁面或打開結帳 modal 的邏輯
  }
}
