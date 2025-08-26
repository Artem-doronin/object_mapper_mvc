package com.example.object_mapper_mvc.repository;

import com.example.object_mapper_mvc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
