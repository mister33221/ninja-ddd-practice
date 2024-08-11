import { Injectable } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginModalComponent } from 'src/app/login-modal/login-modal.component';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(private modalService: BsModalService) {}

  showLoginModal(): Observable<any> {
    const modalRef: BsModalRef = this.modalService.show(LoginModalComponent);
    return new Observable(observer => {
      modalRef.content.onClose = () => {
        observer.next();
        observer.complete();
      };
    });
  }

  login(username: string, password: string) {
    // 實現實際的登錄邏輯
    // 這裡只是一個簡單的模擬
    if (username && password) {
      this.isLoggedInSubject.next(true);
      return true;
    }
    return false;
  }

  logout() {
    this.isLoggedInSubject.next(false);
  }
}
