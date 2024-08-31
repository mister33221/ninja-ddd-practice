import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { LoginModalComponent } from 'src/app/login-modal/login-modal.component';
import { environment } from 'src/environments/environment';
import { AlertService, AlertType } from '../components/alert/service/alert.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(
    private modalService: BsModalService,
    private httpClient: HttpClient,
    private alertService: AlertService,
    private router: Router,
  ) {
    this.checkInitialLoginStatus();
  }

  private checkInitialLoginStatus() {
    // 檢查 cookie 中是否有有效的 JWT
    const jwt = this.getJwtFromLocalStorage();
    if (jwt && !this.isTokenExpired(jwt)) {
      this.isLoggedInSubject.next(true);
    }
  }

  private isTokenExpired(token: string): boolean {
    const jwtPayload = JSON.parse(atob(token.split('.')[1]));
    return jwtPayload.exp * 1000 < Date.now();
  }

  private getJwtFromLocalStorage(): string | null {
    return localStorage.getItem('jwt');
  }

  showLoginModal(): Observable<any> {
    const modalRef: BsModalRef = this.modalService.show(LoginModalComponent);
    return new Observable((observer) => {
      modalRef.content.onClose = () => {
        observer.next();
        observer.complete();
      };
    });
  }

  /**
   * login and save token to local storage
   */
  login(value: any): Observable<any> {
    return this.httpClient
      .post<any>(environment.BASE_URL + '/user/login', value)
      .pipe(
        map((res) => {
          if (res && res.token) {
            localStorage.setItem('jwt', res.token);
            this.isLoggedInSubject.next(true);
          }
          return res;
        })
      );
  }

  logout() {
    // 清除 local storage 中的 JWT
    localStorage.removeItem('jwt');
    // 發送一個事件，告訴應用程序用戶已經登出
    this.isLoggedInSubject.next(false);
    // 重定向到首頁
    // window.location.href = '/';
    this.router.navigate(['/product-list']);
    // 顯示一個提示消息
    this.alertService.showAlert(AlertType.SUCCESS, '您已登出', 3000);
  }
}
