package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.OrdersImplementation;
import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.SecurityMaster;
import com.cashEquityProject.cashEquity.model.SecurityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class OrderController {

    @Autowired
    OrdersImplementation ordersImplementation;

    @RequestMapping(value="/addOrder")
    public String addOrder(@RequestBody Order order){
        ordersImplementation.addOrder(order);
        return "Data saved successfully";
    }

    @RequestMapping(value="/getOrders/{code}")
    public List<Order> getOrders(@PathVariable String code){
        return ordersImplementation.getOrders(code);
    }

    @RequestMapping(value = "/displaySecurity/{time}")
    public SecurityMaster displaySecurity(String time) {
        return ordersImplementation.displaySecurity(time);
    }

    @RequestMapping(value="/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable String orderId){
        ordersImplementation.deleteOrder(orderId);
        return "Data deleted successfully";
    }
}
