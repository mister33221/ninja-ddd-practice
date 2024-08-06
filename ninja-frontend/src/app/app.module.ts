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

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    LoginModalComponent,
    RegistrationComponent
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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
