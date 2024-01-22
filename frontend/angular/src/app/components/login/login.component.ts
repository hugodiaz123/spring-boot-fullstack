import { Component } from '@angular/core';
import {AuthenticationRequest} from "../../models/authentication-request";
import {AuthenticationService} from "../../services/auth/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authenticationRequest: AuthenticationRequest = {};
  errorMsg: string = '';

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  login() {
    this.errorMsg = '';
    this.authenticationService.login(this.authenticationRequest)
      .subscribe({
        next: (authenticationResponse) => {
          localStorage.setItem('user', JSON.stringify(authenticationResponse));
          this.router.navigate(['customers']);
        },
        error: err => {
          if (err.error.statusCode === 401) {
            this.errorMsg = 'Incorrect email or password';
          }
        }
      })
  }

  register() {
    this.router.navigate(['register']);
  }
}
