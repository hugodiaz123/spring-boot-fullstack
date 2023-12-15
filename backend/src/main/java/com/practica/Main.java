package com.practica;

import com.github.javafaker.Faker;
import com.practica.customer.Customer;
import com.practica.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker fakeCustomer = new Faker();
            Random random = new Random();
            String name = fakeCustomer.name().firstName();
            Customer customer = new Customer(name, name.toLowerCase() + "@gmail.com", random.nextInt(16, 99));

            customerRepository.save(customer);
        };
    }
}