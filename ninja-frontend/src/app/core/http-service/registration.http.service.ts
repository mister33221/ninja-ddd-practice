import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { RegistryHttpModel } from 'src/app/registration/models/registry.http.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegistrationHttpService {


  constructor(
    private httpClient: HttpClient
  ) { }

  register(formData: any): Observable<RegistryHttpModel> {
    return this.httpClient.post<RegistryHttpModel>(environment.BASE_URL + '/user/registry', formData);
  }

}
