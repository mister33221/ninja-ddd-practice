import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { GetUserProfileResponse } from 'src/app/profile/models/GetUserProfileResponse';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserHttpService {
  constructor(private http: HttpClient) {}

  getUserInfoById(userId: string): Observable<GetUserProfileResponse> {
    return this.http.get<GetUserProfileResponse>(environment.BASE_URL + `/user/get-user-info-by-id/${userId}`);

    // return new Observable((subscriber) => {
    //   subscriber.next({
    //     email: 'aaaaa@gmail.com',
    //     username: 'aaaaa',
    //     fullname: 'aaaaa',
    //     phonenumber: '0912345678',
    //     dateofbirth: '2021-01-01',
    //     address: '台北市',
    //   });
    //   subscriber.complete();
    // });
  }
}
