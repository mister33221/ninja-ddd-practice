import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginModalComponent } from 'src/app/login-modal/login-modal.component';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(
    private modalService: BsModalService,
    private httpClient: HttpClient
  ) {
    this.checkInitialLoginStatus();
  }

  private checkInitialLoginStatus() {
    // 檢查 cookie 中是否有有效的 JWT
    const jwt = this.getJwtFromCookie();
    if (jwt && !this.isTokenExpired(jwt)) {
      this.isLoggedInSubject.next(true);
    }
  }

  private isTokenExpired(token: string): boolean {
    const jwtPayload = JSON.parse(atob(token.split('.')[1]));
    return jwtPayload.exp * 1000 < Date.now();
  }

  private getJwtFromCookie(): string | null {
    const jwtCookie = document.cookie.split('; ').find(row => row.startsWith('jwt='));
    return jwtCookie ? jwtCookie.split('=')[1] : null;
  }

  showLoginModal(): Observable<any> {
    const modalRef: BsModalRef = this.modalService.show(LoginModalComponent);
    return new Observable(observer => {
      modalRef.content.onClose = () => {
        observer.next();
        observer.complete();
      };
    });
  }

  login(value: any): Observable<any> {
    return this.httpClient.post<any>(environment.BASE_URL + '/user/login', value);
  }

  logout() {
    // 清除 cookie
    document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    this.isLoggedInSubject.next(false);
  }
}
