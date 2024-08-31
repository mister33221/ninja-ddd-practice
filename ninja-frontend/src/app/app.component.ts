import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AuthService } from './core/auth/auth.service';
import { AlertService, AlertType } from './core/components/alert/service/alert.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = '木葉村忍具店';
  isLoggedIn = false; // 這應該根據實際的登錄狀態來設置
  userName = '鳴人'; // 這應該是實際登錄用戶的名字
  isLoggedIn$ = this.authService.isLoggedIn$;

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService,
    private alertService: AlertService
  ) {}

  // https://ninja-backend.onrender.com/hello/world
  test() {
    this.httpClient
      .get('https://ninja-backend.onrender.com/hello/world', {
        responseType: 'text',
      })
      .subscribe({
        next: (response) => {
          console.log(response);
          alert(response);
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  openLoginModal() {
    this.authService.showLoginModal();
  }

  logout() {
    this.authService.logout();
    // 顯示一個提示消息
    this.alertService.showAlert(AlertType.SUCCESS, '您已登出', 3000);
  }
}
