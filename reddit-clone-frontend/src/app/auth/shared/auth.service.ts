import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SignUpRequestPayload } from 'src/app/components/auth/signup/signup-request.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  /**
   * Signup Method that makes a post call.
   */

  signup(signupRequestPayload:SignUpRequestPayload):Observable<any>{

    return this.httpClient.post('http://localhost:8080/api/auth/signup',signupRequestPayload,{responseType:'text'});

  }


}
