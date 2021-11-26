import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { SignUpRequestPayload } from './signup-request.payload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: SignUpRequestPayload;

  signupForm! : FormGroup

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) { 
    this.signupRequestPayload= {
      username:'',
      email:'',
      password:''
    }
  }

  ngOnInit(): void {
    this.signupForm = new FormGroup(
      {
        // adding form control and validations
        username!: new FormControl('',Validators.required),
        email! : new FormControl('', [Validators.required, Validators.email]),
        password! : new FormControl('', Validators.required)
      }
    )
  }

  /**
   * Read from from group in assign to payload.
   * Access using get method
   */
  signup(){
    this.signupRequestPayload.username = this.signupForm.get('username')?.value;
    this.signupRequestPayload.email = this.signupForm.get('email')?.value;
    this.signupRequestPayload.password = this.signupForm.get('password')?.value;

    this.authService.signup(this.signupRequestPayload)


    .subscribe(
      {
        next : data => {this.router.navigate(['/login'], { queryParams:{registered: 'true'}})},
        error: error => this.toastr.error('Registration failed try again')
      }
      );
  

  }


}
