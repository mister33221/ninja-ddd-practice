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
export class LoginModalComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  loginForm: FormGroup = new FormGroup({});
  constructor(
    public modalRef: BsModalRef,
    // 路由
    private router: Router,
    private loginHttpSvc: LoginHttpService,
    private formBuilder: FormBuilder,
    private alertService: AlertService,
    private authService: AuthService
  ) {}
  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSubmit() {
    this.authService
      .login(this.loginForm.value)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res) => {
          if (res) {
            this.alertService.showAlert(
              'success',
              '登入成功！',
              3000
            );
            this.modalRef.hide();
            this.router.navigate(['/']);
          }
        },
        error: (err) => {
          this.alertService.showAlert('danger', '登入失敗！', 3000);
        },
      });
  }

  /**
   *
   */
  redirectToRegistration() {
    this.modalRef.hide();
    this.router.navigate(['/registration']);
  }

}
