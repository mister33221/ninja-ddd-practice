import { Router, Routes } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/internal/Subject';
import { Form, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';
import { LoginHttpService } from '../core/http-service/login.http.service';
import { AlertService } from '../core/components/alert/service/alert.service';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss'],
})
export class LoginModalComponent implements OnInit {
  loginForm: FormGroup = new FormGroup({});
  constructor(
    public modalRef: BsModalRef,
    // 路由
    private readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly alertService: AlertService,
    private readonly authService: AuthService
  ) {}
  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  /**
   * 登入
   */
  onSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        if (res) {
          this.alertService.showAlert('success', '登入成功！', 3000);
          this.modalRef.hide();
          this.router.navigate(['/']);
        }
      },
    });
  }

  /**
   * 跳轉到註冊頁面
   */
  redirectToRegistration() {
    this.modalRef.hide();
    this.router.navigate(['/registration']);
  }
}
