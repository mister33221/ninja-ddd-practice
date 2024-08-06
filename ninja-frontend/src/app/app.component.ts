import { Component, TemplateRef } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = '木葉村忍具店';
  isLoggedIn = false; // 這應該根據實際的登錄狀態來設置
  userName = '鳴人'; // 這應該是實際登錄用戶的名字

}
