import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
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
  registrationSuccessMessage: string = '';


  constructor(private authService: AuthService, 
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
    ) { 
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

    this.activatedRoute.queryParams
    .subscribe(
      params => {
        if(params['registered'] !== undefined && params['registered'] === 'true'){
          this.toastr.success('Signup Successful');
          this.registrationSuccessMessage = 'Please check your inbox for activation link. Activate your account before login';
        }
      }
      
    )

  }

  login(){
    this.submitted = true;
    this.loginRequestPayLoad.username=this.loginForm.get('username')?.value;
    this.loginRequestPayLoad.password=this.loginForm.get('password')?.value;
    
    this.authService.login(this.loginRequestPayLoad)
    .subscribe(
    {
      next : data =>{
        this.hasError = false;
        this.router.navigateByUrl('/');
      } ,
      error: error => {
                      this.hasError = true
                    },
      complete: () => console.log("Complete")
    }
    );

  }


}
