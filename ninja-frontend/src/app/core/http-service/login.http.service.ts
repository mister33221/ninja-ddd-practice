import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginHttpService {

  constructor(private httpClient: HttpClient) { }

  login(value: any): Observable<any> {
    return this.httpClient.post<any>(environment.BASE_URL + '/user/login', value);
  }


}
