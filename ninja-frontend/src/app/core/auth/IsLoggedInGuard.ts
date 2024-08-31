import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router,
} from '@angular/router';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';


export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn$.pipe(
    map((isLoggedIn) => {
      if (isLoggedIn) {
        alert('You are logged in');
        return true;
      } else {
        alert('You are not logged in');
        return router.createUrlTree(['/product-list']);
      }
    })
  );

};
