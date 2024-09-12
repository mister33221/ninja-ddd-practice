import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';
import {
  AlertService,
  AlertType,
} from '../components/alert/service/alert.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const alertService = inject(AlertService);

  return authService.isLoggedIn$.pipe(
    map((isLoggedIn) => {
      if (isLoggedIn) {
        return true;
      } else {
        alertService.showAlert(
          AlertType.ERROR,
          'You have no permission to access this page',
          3000
        );
        return router.createUrlTree(['/product-list']);
      }
    })
  );
};
