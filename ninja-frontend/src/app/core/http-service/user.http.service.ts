import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { GetUserProfileResponse } from 'src/app/profile/models/GetUserProfileResponse';
import { UpdateUserProfileRequest } from 'src/app/profile/models/UpdateUserProfileRequest';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserHttpService {

  constructor(private http: HttpClient) {}

  getUserInfoById(userId: string): Observable<GetUserProfileResponse> {
    return this.http.get<GetUserProfileResponse>(environment.BASE_URL + `/user/get-user-info-by-id/${userId}`);
  }

  updateUserInfo(updateUserProfileRequest: UpdateUserProfileRequest): Observable<void> {
    return this.http.put<void>(environment.BASE_URL + '/user/update-user-info', updateUserProfileRequest);
  }
}
