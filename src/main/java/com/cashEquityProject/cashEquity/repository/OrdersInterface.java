package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.SecurityMaster;

import java.util.List;

public interface OrdersInterface {

    void addOrder(Order order);

    List<Order> displayOrders(String code);

    void deleteOrder(String orderId);

    public SecurityMaster displaySecurity(String time);
}

