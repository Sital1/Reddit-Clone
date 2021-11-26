import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { LoginRequestPayload } from './login-request.payload';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  loginRequestPayLoad: LoginRequestPayload;
  submitted = false;
  hasError = false;


  constructor(private authService: AuthService) { 
    this.loginRequestPayLoad ={
      username: '',
      password: ''
    }
  }

  ngOnInit(): void {

    this.loginForm = new FormGroup(
      {
        username : new FormControl('', Validators.required),
        password  :new FormControl('', Validators.required)
      }
    )

  }

  login(){
    this.submitted = true;
    this.hasError=false;
    this.loginRequestPayLoad.username=this.loginForm.get('username')?.value;
    this.loginRequestPayLoad.password=this.loginForm.get('password')?.value;
    
    this.authService.login(this.loginRequestPayLoad)
    .subscribe(
    {
      next : data => console.log("loginSuccessfull" + data),
      error: error => {
                      this.hasError = true
                      console.log(error);  
                    },
      complete: () => console.log("Complete")
    }
    );

  }


}
