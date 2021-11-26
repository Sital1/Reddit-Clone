import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, switchMap, throwError,take, filter } from 'rxjs';
import { AuthService } from './auth/shared/auth.service';
import { LoginResponse } from './components/auth/login/login-response.payload';

@Injectable(
  {
    providedIn: 'root'
  }
)
export class TokenInterceptor implements HttpInterceptor {

  isTokenRefreshing = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);
  constructor(public authService: AuthService) { }

  intercept(req: HttpRequest<any>,
      next: HttpHandler): Observable<HttpEvent<any>> {
        if(this.authService.getJwtToken()){
            req = req.clone(
                {
                    setHeaders:{
                        Authorization:`Bearer ${this,this.authService.getJwtToken()}`
                    }
                }
            )
        }
      return next.handle(req).pipe(catchError(error => {
          if (error instanceof HttpErrorResponse
              && error.status === 403) {
              return this.handleAuthErrors(req, next);
          } else {
              return throwError(() => error);
          }
      }));
  }
  private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler)
  : Observable<HttpEvent<any>> {
  if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
          switchMap((refreshTokenResponse: LoginResponse) => {
              this.isTokenRefreshing = false;
              this.refreshTokenSubject
                  .next(refreshTokenResponse.authenticationToken);
              return next.handle(this.addToken(req,
                  refreshTokenResponse.authenticationToken));
          })
      )
  } else {
      return this.refreshTokenSubject.pipe(
          filter(result => result !== null),
          take(1),
          switchMap((res) => {
              return next.handle(this.addToken(req,
                  this.authService.getJwtToken()))
          })
      );
  }
}
  private addToken(req: HttpRequest<any>, jwtToken: string) {
    console.log("tpken added ") 
    console.log(jwtToken)
    return req.clone({
          headers: req.headers.set('Authorization',
              'Bearer ' + jwtToken)
      });
  }

}