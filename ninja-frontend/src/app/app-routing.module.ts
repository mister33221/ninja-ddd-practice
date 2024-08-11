import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';
import { RegistrationComponent } from './registration/registration.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { ProductInventoryComponent } from './product-inventory/product-inventory.component';
import { OrderListComponent } from './order-list/order-list.component';
import { SupplierOrderListComponent } from './supplier-order-list/supplier-order-list.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {
    path: '', redirectTo: '/product-list', pathMatch: 'full'
  },
  {
    path: 'product-list',
    component: ProductListComponent
  },
  {
    path: 'registration',
    component: RegistrationComponent
  },
  {
    path: 'shopping-cart',
    component: ShoppingCartComponent
  },
  {
    path: 'product-invetory',
    component: ProductInventoryComponent
  },
  {
    path: 'order-list',
    component: OrderListComponent
  },
  {
    path: 'supplier-order-list',
    component: SupplierOrderListComponent
  },
  {
    path: 'profile',
    component: ProfileComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
