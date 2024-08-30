import { Component } from '@angular/core';
import { AlertService } from './service/alert.service';
import { Subscription } from 'rxjs/internal/Subscription';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent {
  alerts: Array<{ type: string; msg: string; timeout: number }> = [];
  private subscription: Subscription = new Subscription;

  constructor(private alertService: AlertService) {}

  ngOnInit() {
    this.subscription = this.alertService.alertState$.subscribe(alert => {
      this.addAlert(alert.type, alert.msg, alert.timeout);
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  addAlert(type: string, msg: string, timeout: number): void {
    this.alerts.push({ type, msg, timeout });
  }

  onClosed(dismissedAlert: { type: string; msg: string; timeout: number }): void {
    this.alerts = this.alerts.filter(alert => alert !== dismissedAlert);
  }
}
