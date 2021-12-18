import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/shared/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn?: boolean;
  username?: string

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.getJwtToken();
    this.username = this.authService.getUsername();
  }

  goToUserProfile(){
    this.router.navigateByUrl('/user-profile/');
  }

  logout(){
    
  }

}
