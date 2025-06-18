import {
  Component,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {
  AbstractControlOptions,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { AlertService } from '../core/components/alert/service/alert.service';
import { RegistrationHttpService } from '../core/http-service/registration.http.service';

type ExampleAlertType = { type: string; msg: string; timeout: number };

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registerForm: FormGroup = new FormGroup({});
  modalRef?: BsModalRef;
  @ViewChild('template', { static: true }) template!: TemplateRef<void>;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly registrationHttpSvc: RegistrationHttpService,
    // 導頁服務
    private readonly router: Router,
    private readonly alertService: AlertService
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
      { validator: this.passwordMatchValidator } as AbstractControlOptions
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

      this.registrationHttpSvc.register(formData).subscribe({
        next: (response) => {
          this.alertService.showAlert('success', '註冊成功！請重新登入', 3000);
          this.router.navigate(['/product-list']);
        },
        error: (error) => {
          this.alertService.showAlert('danger', error.error.message, 3000);
        },
      });
    }
  }
}
