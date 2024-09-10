import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthService } from './core/auth/auth.service';
import { AlertService, AlertType } from './core/components/alert/service/alert.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = '木葉村忍具店';
  userName$ = this.authService.userName$;
  isLoggedIn$ = this.authService.isLoggedIn$;

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.authService.checkAuth();
  }

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
    // this.alertService.showAlert(AlertType.SUCCESS, '您已登出', 3000);
  }
}
