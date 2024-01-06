package com.practica.auth;

import com.practica.customer.CustomerDTO;

public record AuthenticationResponse(String token, CustomerDTO customerDTO) {
}
