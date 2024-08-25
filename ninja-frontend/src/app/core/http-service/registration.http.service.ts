import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegistrationHttpService {


  constructor(
    private httpClient: HttpClient
  ) { }

  register(formData: any): Observable<any> {
    return this.httpClient.post<any>(environment.BASE_URL + '/user/registry', formData);
  }

}
