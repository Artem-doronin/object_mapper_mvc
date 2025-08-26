package com.example.object_mapper_mvc.repository;

import com.example.object_mapper_mvc.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
