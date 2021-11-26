import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { map, Observable, tap } from 'rxjs';
import { LoginRequestPayload } from 'src/app/components/auth/login/login-request.payload';
import { LoginResponse } from 'src/app/components/auth/login/login-response.payload';
import { SignUpRequestPayload } from 'src/app/components/auth/signup/signup-request.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService) { }

  /**
   * Signup Method that makes a post call.
   */

  signup(signupRequestPayload:SignUpRequestPayload):Observable<any>{

    return this.httpClient.post('http://localhost:8080/api/auth/signup',signupRequestPayload,{responseType:'text'});

  }

  login(loginRequestPayload:LoginRequestPayload): Observable<Boolean>{

   return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login', loginRequestPayload)
    .pipe(
      map(data => {
         this.localStorage.store('authenticationToken',data.authenticationToken);
         this.localStorage.store('username',data.username);
         this.localStorage.store('refreshToken',data.refreshToken);
         this.localStorage.store('expiresAt',data.expiresAt);

         return true;
      })
    )
  }

  getJwtToken(){
    return this.localStorage.retrieve('authenticationToken');
  }

  getRefreshToken(){
    return this.localStorage.retrieve('refreshToken');
  }

  getUsername(){
    return this.localStorage.retrieve('username');
  }

  refreshToken() {
    const refreshTokenPayLoad = {
      refreshToken: this.getRefreshToken(),
      username: this.getUsername()
    }

    return this.httpClient.post<LoginResponse>('http:localhost:8080/api/auth/refresh/token', refreshTokenPayLoad)
    .pipe(
      tap(response => {
        this.localStorage.store('authenticationToken',response.authenticationToken);
        this.localStorage.store('expiresAt',response.expiresAt);
      })
    );
  }



}
