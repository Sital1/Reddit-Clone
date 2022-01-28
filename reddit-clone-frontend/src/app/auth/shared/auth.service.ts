import { HttpClient } from '@angular/common/http';
import { Injectable, Output,EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { map, Observable, tap, throwError } from 'rxjs';
import { LoginRequestPayload } from 'src/app/components/auth/login/login-request.payload';
import { LoginResponse } from 'src/app/components/auth/login/login-response.payload';
import { SignUpRequestPayload } from 'src/app/components/auth/signup/signup-request.payload';
import { environment } from 'src/environments/environment.prod';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  baseUrl = environment.baseUrl;

  @Output() loggedIn: EventEmitter<boolean>= new EventEmitter();
  @Output() username: EventEmitter<string>= new EventEmitter();

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService,
    private router: Router) { }

  /**
   * Signup Method that makes a post call.
   */

  signup(signupRequestPayload:SignUpRequestPayload):Observable<any>{

    return this.httpClient.post(`${this.baseUrl}api/auth/signup`,signupRequestPayload,{responseType:'text'});

  }

  login(loginRequestPayload:LoginRequestPayload): Observable<Boolean>{

   return this.httpClient.post<LoginResponse>(`${this.baseUrl}api/auth/login`, loginRequestPayload)
    .pipe(
      map(data => {
         this.localStorage.store('authenticationToken',data.authenticationToken);
         this.localStorage.store('username',data.username);
         this.localStorage.store('refreshToken',data.refreshToken);
         this.localStorage.store('expiresAt',data.expiresAt);

         this.loggedIn.emit(true);
         this.username.next(data.username);
        
         return true;
      })
    )
  }

  logout() {
    this.httpClient.post(`${this.baseUrl}api/auth/logout`, this.refreshTokenPayload,
      { responseType: 'text' })
      .subscribe({
       next: data => {
    
        },
        error: error => {
          throwError(()=> error);
        }
      })
    this.localStorage.clear('authenticationToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
    this.loggedIn.emit(false);
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


  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUsername()
  }
  
  refreshToken() {
    return this.httpClient.post<LoginResponse>(`${this.baseUrl}api/auth/refresh/token`,
      this.refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.clear('authenticationToken');
        this.localStorage.clear('expiresAt');

        this.localStorage.store('authenticationToken',
          response.authenticationToken);
        this.localStorage.store('expiresAt', response.expiresAt);
      }));
  }

  isLoggedIn(){
    return this.getJwtToken() != null;
  }


}
