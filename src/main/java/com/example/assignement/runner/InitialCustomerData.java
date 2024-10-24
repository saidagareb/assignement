package com.example.assignement.runner;

import com.example.assignement.domains.Customer;
import com.example.assignement.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Component

public class InitialCustomerData implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Customer> customers = Arrays.asList(
                new Customer("Arisha Barron"),
                new Customer("Branden Gibson"),
                new Customer("Rhonda Church"),
                new Customer("Georgina Hazel"));

        // Save customers to the repository
        customerRepository.saveAll(customers);
    }
}
