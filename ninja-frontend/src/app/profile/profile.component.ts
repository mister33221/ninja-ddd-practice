import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  editForm: FormGroup = new FormGroup({});
  isEditing: boolean = false;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    const userData = this.getUserData();
    this.initForm(userData);
  }

  initForm(userData: any): void {
    this.editForm = this.formBuilder.group({
      email: [{ value: userData.email, disabled: true }],
      password: ['', [Validators.minLength(6)]],
      confirmPassword: [''],
      username: [{ value: userData.username, disabled: true }, [Validators.required, Validators.minLength(3)]],
      fullname: [{ value: userData.fullname, disabled: true }, Validators.required],
      phonenumber: [{ value: userData.phonenumber, disabled: true }, [Validators.required, Validators.pattern(/^\d{10}$/)]],
      dateofbirth: [{ value: userData.dateofbirth, disabled: true }, Validators.required],
      address: [{ value: userData.address, disabled: true }, Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    if (form.get('password')?.value !== form.get('confirmPassword')?.value) {
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
      const updatedData = this.getUserData();
      this.initForm(updatedData);
    }
  }

  getUserData() {
    return {
      email: 'user@example.com',
      username: 'username',
      fullname: 'Full Name',
      phonenumber: '1234567890',
      dateofbirth: '1990-01-01',
      address: '123 Main St'
    };
  }
}
