package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.OrdersImplementation;
import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class OrderController {

    @Autowired
    OrdersImplementation ordersImplementation;

    @RequestMapping(value="/addOrder")
    public String addOrder(@RequestBody Order order){
        ordersImplementation.addOrder(order);
        return "Data saved successfully";
    }

    @RequestMapping(value="/displayOrders/{code}")
    public List<Order> displayOrders(String code){
        List<Order> list = ordersImplementation.displayOrders(code);
        return list;
    }

    @RequestMapping(value="/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable String orderId){
        ordersImplementation.deleteOrder(orderId);
        return "Data deleted successfully";
    }
}
