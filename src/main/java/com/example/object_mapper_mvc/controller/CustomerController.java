package com.example.object_mapper_mvc.controller;

import com.example.object_mapper_mvc.model.Customer;
import com.example.object_mapper_mvc.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<String> createCustomer( @RequestBody String customerJson) {
        try {
            Customer customer = objectMapper.readValue(customerJson, Customer.class);
            validateCustomer(customer);
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(objectMapper.writeValueAsString(createdCustomer));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer data");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody String customerJson) {
        try {
            Customer customer = objectMapper.readValue(customerJson, Customer.class);
            validateCustomer(customer);
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(objectMapper.writeValueAsString(updatedCustomer));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer data");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    private void validateCustomer(Customer customer) {
        if (customer.getFirstName() == null || customer.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is mandatory");
        }
        if (customer.getLastName() == null || customer.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is mandatory");
        }
        if (customer.getEmail() == null || !customer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email should be valid");
        }
        if (customer.getContactNumber() == null || customer.getContactNumber().isEmpty()) {
            throw new IllegalArgumentException("Contact number is mandatory");
        }
    }
}

