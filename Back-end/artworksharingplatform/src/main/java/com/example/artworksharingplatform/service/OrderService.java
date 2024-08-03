package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import com.example.artworksharingplatform.entity.Order;

public interface OrderService {
    Order addOrder(Order order) throws Exception;

    long countOrders();

    List<Order> getOrdersByUserId(UUID userId);
}
