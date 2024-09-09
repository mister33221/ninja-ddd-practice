import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserHttpService } from '../core/http-service/user.http.service';
import { Subject } from 'rxjs/internal/Subject';
import { AuthService, UserInfo } from '../core/auth/auth.service';
import { AlertService } from '../core/components/alert/service/alert.service';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { take, takeUntil } from 'rxjs';
import { GetUserProfileResponse } from './models/GetUserProfileResponse';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();
  isLoggedIn$ = this.authService.isLoggedIn$;
  editForm: FormGroup = new FormGroup({});
  isEditing: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private alertService: AlertService,
    private userHttpService: UserHttpService
  ) { }

  ngOnInit(): void {
    this.getUserData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  initForm(userProfile: GetUserProfileResponse): void {
    this.editForm = this.formBuilder.group({
      email: [{ value: 123, disabled: true }],
      password: ['', [Validators.minLength(6)]],
      confirmPassword: [''],
      username: [{ value: 123, disabled: true }, [Validators.required, Validators.minLength(3)]],
      fullName: [{ value: userProfile.fullName, disabled: true }, Validators.required],
      phoneNumber: [{ value: userProfile.phoneNumber, disabled: true }, [Validators.required, Validators.pattern(/^\d{10}$/)]],
      dateOfBirth: [{ value: userProfile.dateOfBirth, disabled: true }, Validators.required],
      address: [{ value: userProfile.address, disabled: true }, Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    if (password !== confirmPassword) {
      form.get('confirmPassword')?.setErrors({ mismatch: true });
    } else {
      form.get('confirmPassword')?.setErrors(null);
    }
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (this.isEditing) {
      this.editForm.enable();
      this.editForm.get('email')?.disable();
    } else {
      if (this.editForm.valid) {
        this.onSubmit();
      }
      this.editForm.disable();
    }
  }

  onSubmit(): void {
    if (this.editForm.valid) {
      console.log(this.editForm.value);
      // 這裡可以調用您的更新服務
      // 更新成功後可以調用 this.getUserData() 來獲取最新數據
      // const updatedData = this.getUserData();
      // this.initForm(updatedData);
    }
  }

  getUserData(): void {
    const userId = this.authService.getJwtPayloadAttr(UserInfo.ID)
    if (!userId) {
      this.alertService.showAlert('danger', '用戶ID無效，無法獲取用戶數據！　', 3000);
      return;
    }
    this.userHttpService.getUserInfoById(userId)
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: (getUserInfoResponse) => {
        alert('getUserInfoResponse: ' + JSON.stringify(getUserInfoResponse));
        this.initForm(getUserInfoResponse);
      },
      error: (error) => {
        this.alertService.showAlert('danger', '獲取用戶數據失敗！　' + error.error.message, 3000);
      },
    });
  }
}
