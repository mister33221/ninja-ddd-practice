import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as bcrypt from 'bcryptjs';
import { RegistrationHttpService } from '../core/http-service/registration.http.service';
import { Router } from '@angular/router';

type ExampleAlertType = { type: string; msg: string; timeout: number };

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registerForm: FormGroup = new FormGroup({});

  constructor(
    private formBuilder: FormBuilder,
    private registrationHttpSvc: RegistrationHttpService,
    // 導頁服務
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, Validators.minLength(3)]],
        fullName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phoneNumber: [
          '',
          [Validators.required, Validators.pattern(/^\d{10}$/)],
        ],
        dateOfBirth: ['', Validators.required],
        address: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validator: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(form: FormGroup) {
    if (form.get('password')?.value !== form.get('confirmPassword')?.value) {
      form.get('confirmPassword')?.setErrors({ mismatch: true });
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formData = { ...this.registerForm.value };

      // salting 加鹽
      const salt = bcrypt.genSaltSync(10);
      formData.password = bcrypt.hashSync(
        this.registerForm.value.password,
        salt
      );
      formData.confirmPassword = bcrypt.hashSync(
        this.registerForm.value.confirmPassword,
        salt
      );

      // 將密碼加密後的資料送出
      this.registrationHttpSvc.register(formData).subscribe({
        next: (response) => {
          console.log('Registration response:', response);
        },
        error: (error) => {
          console.error('Registration error:', error);
        },
      });
    }
  }


  alerts: ExampleAlertType[] = [
    {
      type: 'success',
      msg: `Well done! You successfully read this important alert message. (added: ${new Date().toLocaleTimeString()})`,
      timeout: 5000
    }
  ];

  add(): void {
    this.alerts.push({
      type: 'info',
      msg: `This alert will be closed in 5 seconds (added: ${new Date().toLocaleTimeString()})`,
      timeout: 5000
    });
  }

  onClosed(dismissedAlert: ExampleAlertType): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }
}
