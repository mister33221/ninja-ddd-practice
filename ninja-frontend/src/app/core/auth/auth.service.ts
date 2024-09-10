import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { LoginModalComponent } from 'src/app/login-modal/login-modal.component';
import { environment } from 'src/environments/environment';
import {
  AlertService,
  AlertType,
} from '../components/alert/service/alert.service';
import { Router } from '@angular/router';

export enum UserInfo{
  ID = 'id',
  USERNAME = 'username',
  EMAIL = 'email'
}

export interface JwtPayload {
  id: string;
  username: string;
  email: string;
  exp: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedInSubject.asObservable();
  // 將這個值設定為 private，讓其他元件無法直接修改這個值
  // BehaviorSubject 會保存最新的值，並且當有新的訂閱者時，會立即發送這個值給新的訂閱者
  private userNameSubject$ = new BehaviorSubject<string>('');
  // 這格變數是公開得，讓其他元件可以訂閱這個值，而不是直接修改這個值
  // 這樣可以確保這個值只能在這個服務中被修改
  userName$ = this.userNameSubject$.asObservable();


  constructor(
    private modalService: BsModalService,
    private httpClient: HttpClient,
    private alertService: AlertService,
    private router: Router
  ) {
    this.checkInitialLoginStatus();

  }

  private checkInitialLoginStatus() {
    // 檢查 cookie 中是否有有效的 JWT
    const jwt = this.getAuthorizationFromLocalStorage();
    if (jwt && !this.isAuthorizationExpired(jwt)) {
      this.isLoggedInSubject.next(true);
      this.userNameSubject$.next(this.getAuthorizationPayloadAttr(UserInfo.USERNAME) || '');
    }

  }

  private isAuthorizationExpired(token: string): boolean {
    const jwtPayload = JSON.parse(atob(token.split('.')[1]));
    return jwtPayload.exp * 1000 < Date.now();
  }

  private getAuthorizationFromLocalStorage(): string | null {
    return localStorage.getItem('Authorization');
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
            this.parseJwt(res.token);
            this.isLoggedInSubject.next(true);
            this.userNameSubject$.next(this.getAuthorizationPayloadAttr(UserInfo.USERNAME) || '');
          }
          return res;
        })
      );
  }

  logout() {
    // 清除 local storage 中的 JWT
    localStorage.removeItem('Authorization');
    // 發送一個事件，告訴應用程序用戶已經登出
    this.isLoggedInSubject.next(false);
    // 重定向到首頁
    // window.location.href = '/';
    this.router.navigate(['/product-list']);
    // 顯示一個提示消息
    this.alertService.showAlert(AlertType.SUCCESS, '您已登出', 3000);
  }

  /**
   * 解析 JWT，並把解析出來的資訊及jwt都存到 localStorage
   */
  parseJwt(token: string): void {
    const jwtPayload = JSON.parse(atob(token.split('.')[1]));
    localStorage.setItem('Authorization', token);
    localStorage.setItem('jwtPayload', JSON.stringify(jwtPayload));
  }

  /**
   * 從 localStorage 中獲取 JWT
   */
  getAuthorization(): string | null {
    return localStorage.getItem('Authorization');
  }

  /**
   * 從 localStorage 中的 JWT Payload 獲取特定的屬性(id, username, email)
   */
  getAuthorizationPayloadAttr(attr: string): string | null {
    const jwtPayload = localStorage.getItem('jwtPayload');
    if (jwtPayload) {
      return JSON.parse(jwtPayload)[attr];
    }
    return null;
  }

  /**
   * 每次應用程序啟動時，從 localStorage 中檢查是否有有效的 JWT
   */
  checkAuth() {
    const authorization = this.getAuthorization();
    if (authorization && !this.isAuthorizationExpired(authorization)) {
      this.isLoggedInSubject.next(true);
      this.userNameSubject$.next(this.getAuthorizationPayloadAttr(UserInfo.USERNAME) || '');
    }
  }
}
