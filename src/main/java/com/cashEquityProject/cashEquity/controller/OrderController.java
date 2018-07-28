package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.implementation.OrdersImplementation;
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

        // TODO: Return proper JSON
        return "Data saved successfully";

    }

    @RequestMapping(value="/getOrders/{clientCode}")
    public List<Order> getOrders(@PathVariable String clientCode){

        // Return orders for client with corresponding client code
        return ordersImplementation.getOrders(clientCode);

    }

    @RequestMapping(value="/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable String orderId){

        // Delete an order using orderId
        ordersImplementation.deleteOrder(orderId);

        // TODO: Return proper JSON
        return "Data deleted successfully";

    }
}
