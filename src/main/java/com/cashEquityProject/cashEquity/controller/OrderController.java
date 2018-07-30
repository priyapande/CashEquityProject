package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.implementation.OrdersImplementation;
import com.cashEquityProject.cashEquity.model.Security;
import com.cashEquityProject.cashEquity.repository.config;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class OrderController {

    JSONObject jsonObject = new JSONObject();

    @Autowired
    OrdersImplementation ordersImplementation;

    @RequestMapping(value="/addOrder")
    public String addOrder(@RequestBody Order order){

        try {
            ordersImplementation.addOrder(order);

            // TODO: Return proper JSON
            //return "Data saved successfully";
            jsonObject.put("status", config.SUCCESS);
            return jsonObject.toString();
        }
        catch (Exception e)
        {
            jsonObject.put("status", config.FAILED);
            return jsonObject.toString();

        }

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
        //return "Data deleted successfully";
        jsonObject.put("status", config.SUCCESS);
        return jsonObject.toString();

    }

    @RequestMapping(value="/cancelOrder/{orderId}")
    public String cancelOrder(@PathVariable String orderId){

        // Change status of order using orderId
        ordersImplementation.cancelOrder(orderId);

        jsonObject.put("status", config.SUCCESS);
        return jsonObject.toString();
    }

    @RequestMapping(value="/getTopOrders/{symbol}")
    public List<Order> getTopOrders(@PathVariable String symbol){

        // Return top buy and sell orders for corresponding security symbol
        // The list contains 10 elements, first 5 as the top 5 Buy orders and the rest 5 as the top 5 sell orders
        return ordersImplementation.getTopOrders(symbol);
    }
}
