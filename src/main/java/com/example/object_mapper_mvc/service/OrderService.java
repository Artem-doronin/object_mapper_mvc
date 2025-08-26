package com.example.object_mapper_mvc.service;

import com.example.object_mapper_mvc.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Order order);

    void deleteOrder(Long id);

    Order updateOrder(Long id,Order order);
}
