package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.Security;
import org.json.JSONObject;

import java.util.List;

public interface OrdersInterface {

    // Add order based on Order object
    void addOrder(Order order);

    // Get orders based on client code
    List<Order> getOrders(String code);

    // Delete order based on order id
    void deleteOrder(String orderId, Character direction, String symbol);

    // Cancel order based on order id
    void cancelOrder(String orderId);

    // Returns top 5 sell and buy orders for a particular security
    String getTopOrders(String symbol);

    String getClientReport(String clientCode);

    void updateOrder(Integer orderId, Integer quantity, Double limitPrice);

//    String getOrderById(String orderId);
}

