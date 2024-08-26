import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as bcrypt from 'bcryptjs';
import { RegistrationHttpService } from '../core/http-service/registration.http.service';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

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
  modalTitle: string = '';
  modalContent: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private registrationHttpSvc: RegistrationHttpService,
    // 導頁服務
    private router: Router,
    private modalService: BsModalService
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
          this.openModal('Success', response.message);
          this.router.navigate(['/product-list']);
        },
        error: (error) => {
          this.openModal('Error', error.error.message);
        },
      });
    }
  }


  openModal(title: string, content: string) {
    this.modalTitle = title;
    this.modalContent = content;
    this.modalRef = this.modalService.show(this.template, {
      ariaDescribedby: 'my-modal-description',
      ariaLabelledBy: 'my-modal-title'
    });
  }
}
