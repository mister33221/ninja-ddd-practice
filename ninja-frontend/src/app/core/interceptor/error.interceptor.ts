import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AlertService, AlertType } from '../components/alert/service/alert.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private alertService: AlertService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred!';

        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          errorMessage = this.getErrorMessage(error.error.customCode);
        }

        this.alertService.showAlert(AlertType.DANGER, errorMessage, 3000);

        return throwError(errorMessage);
      })
    );
  }

  private getErrorMessage(customCode: string): string {
    const errorMessages: { [key: string]: string } = {
      'ERR_APPLICATION_0001': 'User not found',
      'ERR_APPLICATION_0002': 'Username already exists',
      'ERR_APPLICATION_0003': 'Email already exists',
      'ERR_APPLICATION_0005': 'Internal server error',
      'ERR_APPLICATION_0006': 'Signature verification failed',
      'ERR_APPLICATION_0007': 'Expired JWT token',
      'ERR_APPLICATION_0008': 'Unsupported JWT token',
      'ERR_APPLICATION_0009': 'Illegal argument',
      'ERR_APPLICATION_0010': 'Invalid password',
      'ERR_APPLICATION_0011': 'Product not found',
      'ERR_APPLICATION_0012': 'Shopping cart not found',
      'ERR_DOMAIN_0001': 'User not found',
      'ERR_DOMAIN_0002': 'Username already exists',
      'ERR_DOMAIN_0003': 'Email already exists',
      'ERR_DOMAIN_0004': 'Invalid input',
      'ERR_DOMAIN_0005': 'Internal server error',
      'ERR_INFRA_0001': 'JWT 結構錯誤',
      'ERR_INFRA_0002': 'JWT 已過期',
      'ERR_INFRA_0003': 'JWT 無效',
      'ERR_INFRA_0004': 'JWT 簽名驗證失敗',
      'ERR_INFRA_0005': 'JWT 為空'
    };

    return errorMessages[customCode] || 'An unknown error occurred!';
  }
}


// USER_NOT_FOUND("ERR_APPLICATION_0001", HttpStatus.UNAUTHORIZED.value(), "User not found"),
// USERNAME_ALREADY_EXISTS("ERR_APPLICATION_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
// EMAIL_ALREADY_EXISTS("ERR_APPLICATION_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
// INTERNAL_SERVER_ERROR("ERR_APPLICATION_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),
// SECURITY_EXCEPTION("ERR_APPLICATION_0006", HttpStatus.UNAUTHORIZED.value(), "Signature verification failed"),
// EXPIRED_JWT_TOKEN("ERR_APPLICATION_0007", HttpStatus.UNAUTHORIZED.value(), "Expired JWT token"),
// UNSUPPORTED_JWT_TOKEN("ERR_APPLICATION_0008", HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"),
// ILLIGAL_ARGUMENT("ERR_APPLICATION_0009", HttpStatus.BAD_REQUEST.value(), "Illegal argument"),
// INVALID_PASSWORD("ERR_APPLICATION_0010", HttpStatus.UNAUTHORIZED.value(), "Invalid password"),
// PRODUCT_NOT_FOUND("ERR_APPLICATION_0011", HttpStatus.NOT_FOUND.value(), "Product not found"),
// SHOPPING_CART_NOT_FOUND("ERR_APPLICATION_0012", HttpStatus.NOT_FOUND.value(), "Shopping cart not found");

// USER_NOT_FOUND("ERR_DOMAIN_0001", HttpStatus.NOT_FOUND.value(), "User not found"),
// USERNAME_ALREADY_EXISTS("ERR_DOMAIN_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
// EMAIL_ALREADY_EXISTS("ERR_DOMAIN_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
// INVALID_INPUT("ERR_DOMAIN_0004", HttpStatus.BAD_REQUEST.value(), "Invalid input"),
// INTERNAL_SERVER_ERROR("ERR_DOMAIN_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");


// JWT_STRUCTURE_ERROR("ERR_INFRA_0001", HttpStatus.UNAUTHORIZED.value(), "JWT 結構錯誤"),
// JWT_EXPIRED("ERR_INFRA_0002", HttpStatus.UNAUTHORIZED.value(), "JWT 已過期"),
// JWT_INVALID("ERR_INFRA_0003", HttpStatus.UNAUTHORIZED.value(), "JWT 無效"),
// JWT_SIGNATURE_VERIFICATION_FAILED("ERR_INFRA_0004", HttpStatus.UNAUTHORIZED.value(), "JWT 簽名驗證失敗"),
// JWT_EMPTY("ERR_INFRA_0005", HttpStatus.UNAUTHORIZED.value(), "JWT 為空");
