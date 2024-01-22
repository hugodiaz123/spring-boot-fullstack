import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";
import {CustomerService} from "../../services/customer/customer.service";
import {AuthenticationService} from "../../services/auth/authentication.service";
import {AuthenticationRequest} from "../../models/authentication-request";
import {AuthenticationResponse} from "../../models/authentication-response";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  errorMsg: any;
  customer: CustomerRegistrationRequest = {}

  constructor(
    private router: Router,
    private customerService: CustomerService,
    private authenticationService: AuthenticationService) {}


  login() {
    this.router.navigate(['login'])
  }

  createAccount() {
    this.customerService.registerCustomer(this.customer).subscribe({
      next: () => {
        const authRequest: AuthenticationRequest = {
          username: this.customer.email,
          password: this.customer.password
        }
        this.authenticationService.login(authRequest).subscribe({
          next: (authResponse: AuthenticationResponse) => {
            localStorage.setItem('user', JSON.stringify(authResponse))
            this.router.navigate(['customers'])
          },
          error: err => {
            if (err.error.statusCode === 403) {
              this.errorMsg = 'The email is already taken';
            }
          }
        })
      }
    })
  }
}
