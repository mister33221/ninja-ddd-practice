import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ProductListComponent } from './product-list/product-list.component';
import { LoginModalComponent } from './login-modal/login-modal.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { ProductInventoryComponent } from './product-inventory/product-inventory.component';
import { OrderListComponent } from './order-list/order-list.component';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { SupplierOrderListComponent } from './supplier-order-list/supplier-order-list.component';
import { ProfileComponent } from './profile/profile.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AlertModule } from 'ngx-bootstrap/alert';
import { AlertComponent } from './core/components/alert/alert.component';
import { AuthInterceptor } from './core/interceptor/auth.interceptor';


@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    LoginModalComponent,
    RegistrationComponent,
    ShoppingCartComponent,
    ProductInventoryComponent,
    OrderListComponent,
    SupplierOrderListComponent,
    ProfileComponent,
    AlertComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, // 當要使用板驅動表單（Template-driven forms）時，需要引入
    // example: <form #f="ngForm" (ngSubmit)="onSubmit(f)">
    ReactiveFormsModule,  // 當要使用響應式表單（Reactive forms）時，需要引入
    // example: <form [formGroup]="form" (ngSubmit)="onSubmit()">
    TooltipModule.forRoot(),
    ModalModule.forRoot(),
    AccordionModule.forRoot(),
    HttpClientModule, // 添加 HttpClientModule 才能使用 HttpClient
    AlertModule.forRoot(),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
