import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AlertService } from '../components/alert/service/alert.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private alertService: AlertService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return next.handle(request);

  }
}


// USER_NOT_FOUND("ERR_0001", HttpStatus.NOT_FOUND.value(), "User not found"),
// USERNAME_ALREADY_EXISTS("ERR_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
// EMAIL_ALREADY_EXISTS("ERR_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
// INVALID_INPUT("ERR_0004", HttpStatus.BAD_REQUEST.value(), "Invalid input"),
// INTERNAL_SERVER_ERROR("ERR_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");

// USER_NOT_FOUND("ERR_0001", HttpStatus.UNAUTHORIZED.value(), "User not found"),
// USERNAME_ALREADY_EXISTS("ERR_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
// EMAIL_ALREADY_EXISTS("ERR_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
// INTERNAL_SERVER_ERROR("ERR_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),
// SECURITY_EXCEPTION("ERR_0006", HttpStatus.UNAUTHORIZED.value(), "Signature verification failed"),
// EXPIRED_JWT_TOKEN("ERR_0007", HttpStatus.UNAUTHORIZED.value(), "Expired JWT token"),
// UNSUPPORTED_JWT_TOKEN("ERR_0008", HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"),
// ILLIGAL_ARGUMENT("ERR_0009", HttpStatus.BAD_REQUEST.value(), "Illegal argument"),
// INVALID_PASSWORD("ERR_0010", HttpStatus.UNAUTHORIZED.value(), "Invalid password"),
// PRODUCT_NOT_FOUND("ERR_0011", HttpStatus.NOT_FOUND.value(), "Product not found"),
// SHOPPING_CART_NOT_FOUND("ERR_0012", HttpStatus.NOT_FOUND.value(), "Shopping cart not found");

// JWT_STRUCTURE_ERROR("SECURITY_001", HttpStatus.UNAUTHORIZED.value(), "JWT 結構錯誤"),
// JWT_EXPIRED("SECURITY_002", HttpStatus.UNAUTHORIZED.value(), "JWT 已過期"),
// JWT_INVALID("SECURITY_003", HttpStatus.UNAUTHORIZED.value(), "JWT 無效"),
// JWT_SIGNATURE_VERIFICATION_FAILED("SECURITY_004", HttpStatus.UNAUTHORIZED.value(), "JWT 簽名驗證失敗"),
// JWT_EMPTY("SECURITY_005", HttpStatus.UNAUTHORIZED.value(), "JWT 為空");


