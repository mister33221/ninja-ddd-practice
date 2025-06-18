import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService, UserInfo } from '../core/auth/auth.service';
import { AlertService } from '../core/components/alert/service/alert.service';
import { UserHttpService } from '../core/http-service/user.http.service';
import { GetUserProfileResponse } from './models/GetUserProfileResponse';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  isLoggedIn$ = this.authService.isLoggedIn$;
  editForm: FormGroup = new FormGroup({});
  isEditing: boolean = false;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly alertService: AlertService,
    private readonly userHttpService: UserHttpService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.getUserData();
  }

  initForm(): void {
    this.editForm = this.formBuilder.group({
      email: [{ value: '', disabled: true }],
      username: [
        { value: '', disabled: true },
        [Validators.required, Validators.minLength(3)],
      ],
      fullName: [{ value: '', disabled: true }, Validators.required],
      phoneNumber: [
        { value: '', disabled: true },
        [Validators.required, Validators.pattern(/^\d{10}$/)],
      ],
      dateOfBirth: [{ value: '', disabled: true }, Validators.required],
      address: [{ value: '', disabled: true }, Validators.required],
    });
  }

  setDataToForm(userProfile: GetUserProfileResponse): void {
    this.editForm = this.formBuilder.group(
      {
        email: [{ value: userProfile.email, disabled: true }],
        username: [
          { value: userProfile.username, disabled: true },
          [Validators.required, Validators.minLength(3)],
        ],
        fullName: [
          { value: userProfile.fullName, disabled: true },
          Validators.required,
        ],
        phoneNumber: [
          { value: userProfile.phoneNumber, disabled: true },
          [Validators.required, Validators.pattern(/^\d{10}$/)],
        ],
        dateOfBirth: [
          { value: userProfile.dateOfBirth, disabled: true },
          Validators.required,
        ],
        address: [
          { value: userProfile.address, disabled: true },
          Validators.required,
        ],
      },
      { validator: this.passwordMatchValidator }
    );
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
      this.userHttpService.updateUserInfo(this.editForm.value).subscribe({
        next: (response) => {
          this.alertService.showAlert('success', '用戶信息更新成功！　', 3000);
        },
        error: (error) => {
          this.alertService.showAlert(
            'danger',
            '用戶信息更新失敗！　' + error.error.message,
            3000
          );
          this.getUserData();
        },
      });
    }
  }

  getUserData(): void {
    const userId = this.authService.getAuthorizationPayloadAttr(UserInfo.ID);
    if (!userId) {
      this.alertService.showAlert(
        'danger',
        '用戶ID無效，無法獲取用戶數據！　',
        3000
      );
      return;
    }
    this.userHttpService.getUserInfoById(userId).subscribe({
      next: (getUserInfoResponse) => {
        this.setDataToForm(getUserInfoResponse);
      },
      error: (error) => {
        this.alertService.showAlert(
          'danger',
          '獲取用戶數據失敗！　' + error.error.message,
          3000
        );
      },
    });
  }
}
