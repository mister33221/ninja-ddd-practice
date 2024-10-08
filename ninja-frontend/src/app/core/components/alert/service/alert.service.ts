import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';

export enum AlertType {
  SUCCESS = 'success',
  DANGER = 'danger',
  WARNING = 'warning',
  INFO = 'info'
}

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private alertSubject = new Subject<{ type: string; msg: string; timeout: number }>();

  alertState$ = this.alertSubject.asObservable();

  showAlert(type: string, msg: string, timeout: number = 5000): void {
    this.alertSubject.next({ type, msg, timeout });
  }

}
