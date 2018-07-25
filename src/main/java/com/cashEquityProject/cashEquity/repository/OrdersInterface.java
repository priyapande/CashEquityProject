package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Order;

import java.util.List;

public interface OrdersInterface {

    void addOrder(Order order);

    List<Order> displayOrders(String code);

    void deleteOrder(String orderId);
}

