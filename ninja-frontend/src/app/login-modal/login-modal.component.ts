import { Router, Routes } from '@angular/router';
import { Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent {
  username: string = '';
  password: string = '';

  constructor(
    public modalRef: BsModalRef,
    // 路由
    private router: Router
  ) {}

  onSubmit() {
    // 在這裡處理登錄邏輯
    console.log('Login attempt', this.username, this.password);
    // 登錄成功後關閉模態框
    this.modalRef.hide();
  }

  // 導向註冊頁面
  redirectToRegistration() {
    this.modalRef.hide();
    this.router.navigate(['/registration']);
  }

}
