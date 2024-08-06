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

  constructor(public modalRef: BsModalRef) {}

  onSubmit() {
    // 在這裡處理登錄邏輯
    console.log('Login attempt', this.username, this.password);
    // 登錄成功後關閉模態框
    this.modalRef.hide();
  }
}
