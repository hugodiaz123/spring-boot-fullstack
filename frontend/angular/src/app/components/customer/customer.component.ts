import {Component, OnInit} from '@angular/core';
import {CustomerDTO} from "../../models/customer-dto";
import {CustomerService} from "../../services/customer/customer.service";
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";
import {ConfirmationService, MessageService} from "primeng/api";
import {AuthenticationResponse} from "../../models/authentication-response";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent implements OnInit{

  display: boolean = false;
  operation: 'create' | 'update' = 'create';
  customers: Array<CustomerDTO> = [];
  customer: CustomerRegistrationRequest = {};

  constructor(
    private customerService: CustomerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.findAllCustomers();
  }

  private findAllCustomers() {
    this.customerService.findAll().subscribe({
      next: data => {this.customers = data; console.log(data)}
    })
  }

  save(customer: CustomerRegistrationRequest) {
    if (customer) {
      if (this.operation === 'create') {
        this.customerService.registerCustomer(customer).subscribe({
          next: () => {
            this.findAllCustomers();
            this.display = false;
            this.customer = {};
            this.messageService.add({
              severity: 'success',
              summary: 'Customer saved',
              detail: `Customer ${customer.name} was successfully saved`
            })
          },
          error: err => {
            if (err.error.statusCode === 500) {
              this.messageService.add({
                severity: 'error',
                summary: 'Email Taken',
                detail: `The email is already taken`
              })
            }
          }
        });
      } else if (this.operation === 'update') {
        this.customerService.updateCustomer(customer.id, customer).subscribe({
          next: () => {
            this.findAllCustomers();
            this.display = false;
            this.customer = {};
            this.messageService.add({
              severity: 'success',
              summary: 'Customer updated',
              detail: `Customer ${customer.name} was successfully updated`
            })
          }
        });
      }
    }
  }

  deleteCustomer(customer: CustomerDTO) {
    this.confirmationService.confirm({
      header: 'Delete customer',
      message: `Are you sure you want to delete ${customer.name}? You can't undo this action afterwards.`,
      accept: () => {
        this.customerService.deleteCustomer(customer.id)
          .subscribe({
            next: () => {
              this.findAllCustomers();
              this.messageService.add({
                severity: 'success',
                summary: 'Customer deleted',
                detail: `Customer ${customer.name} was successfully deleted`
              })
              const currentUser = localStorage.getItem('user');
              if (currentUser) {
                const authResponse: AuthenticationResponse = JSON.parse(currentUser);
                if (authResponse && authResponse.customerDTO && customer.email === authResponse.customerDTO.email) {
                  localStorage.clear()
                  this.router.navigate(['login'])
                }
              }
            }
          })
      }
    })
  }

  updateCustomer(customerDTO: CustomerDTO) {
    this.display = true;
    this.customer = customerDTO;
    this.operation = 'update';
  }

  createCustomer() {
    this.display = true;
    this.customer = {};
    this.operation = 'create';
  }

  cancel() {
    this.display = false;
    this.customer = {};
    this.operation = 'create';
  }
}
