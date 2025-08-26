package com.example.object_mapper_mvc.service;

import com.example.object_mapper_mvc.exeption.ResourceNotFoundException;
import com.example.object_mapper_mvc.model.Order;
import com.example.object_mapper_mvc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order oldOrder = getOrderById(id);
        oldOrder.setOrderDate(order.getOrderDate());
        oldOrder.setOrderStatus(order.getOrderStatus());
        oldOrder.setCustomer(order.getCustomer());
        oldOrder.setShippingAddress(order.getShippingAddress());
        oldOrder.setTotalPrice(order.getTotalPrice());
        oldOrder.setProducts(order.getProducts());

        return orderRepository.save(oldOrder);
    }
}
