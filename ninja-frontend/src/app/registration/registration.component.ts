import {
  Component,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControlOptions } from '@angular/forms';
import { RegistrationHttpService } from '../core/http-service/registration.http.service';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/internal/Subject';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';

type ExampleAlertType = { type: string; msg: string; timeout: number };

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

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

      this.registrationHttpSvc
        .register(formData)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
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
      ariaLabelledBy: 'my-modal-title',
    });
  }
}
