import { Component } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';

interface Product {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
}


@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent {

  constructor(private authService: AuthService) {}

  onSomeAction() {
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      if (!isLoggedIn) {
        this.authService.showLoginModal().subscribe(() => {
          // 登錄模態框關閉後的操作
          this.performActionIfLoggedIn();
        });
      } else {
        this.performActionIfLoggedIn();
      }
    });
  }

  performActionIfLoggedIn() {
    // 執行需要登錄的操作
    console.log('Performing action for logged in user');
  }
}
