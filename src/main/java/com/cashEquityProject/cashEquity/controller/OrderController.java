package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.implementation.OrdersImplementation;
import com.cashEquityProject.cashEquity.repository.config;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class OrderController {

    @Autowired
    private OrdersImplementation ordersImplementation;

    @PostMapping(value="/addOrder")
    public String addOrder(@RequestBody Order order){

        JSONObject jsonObject = new JSONObject();

        try {
            ordersImplementation.addOrder(order);

            jsonObject.put("status", config.SUCCESS);
            return jsonObject.toString();

        } catch (Exception exp) {
            jsonObject.put("status", config.FAILED);
            jsonObject.put("msg", exp.getMessage());
        }

        return jsonObject.toString();

    }

    @RequestMapping(value="/getOrders/{clientCode}")
    public List<Order> getOrders(@PathVariable String clientCode){

        // Return orders for client with corresponding client code
        return ordersImplementation.getOrders(clientCode);

    }

    @RequestMapping(value="/deleteOrder/{orderId}/{direction}/{symbol}")
    public String deleteOrder(@PathVariable String orderId, @PathVariable Character direction, @PathVariable String symbol){

        JSONObject jsonObject = new JSONObject();

        // Delete an order using orderId
        ordersImplementation.deleteOrder(orderId, direction, symbol);

        jsonObject.put("status", config.SUCCESS);
        return jsonObject.toString();


    }
    @RequestMapping(value="/cancelOrder/{orderId}")
    public String cancelOrder(@PathVariable String orderId){

        JSONObject jsonObject = new JSONObject();

        // Change status of order using orderId
        ordersImplementation.cancelOrder(orderId);

        jsonObject.put("status", config.SUCCESS);
        return jsonObject.toString();
    }

    @RequestMapping(value="/getTopOrders/{symbol}")
    public String getTopOrders(@PathVariable String symbol){

        // Return top buy and sell orders for corresponding security symbol
        // The list contains 10 elements, first 5 as the top 5 Buy orders and the rest 5 as the top 5 sell orders
        return ordersImplementation.getTopOrders(symbol);
    }

    @RequestMapping(value = "/getReportByClient/{clientCode}")
    public String getReportByClient(@PathVariable String clientCode) {
        return ordersImplementation.getClientReport(clientCode);
    }

    @RequestMapping(value="/updateOrder/{orderId}/{quantity}/{limitPrice}")
    public String updateOrder(@PathVariable Integer orderId, @PathVariable Integer quantity, @PathVariable Double limitPrice){
        JSONObject jsonObject = new JSONObject();

        ordersImplementation.updateOrder(orderId, quantity, limitPrice);

        jsonObject.put("status", config.SUCCESS);
        return jsonObject.toString();
    }

//    @RequestMapping(value="/getOrderById/{orderId}")
//    public String getOrderById(@PathVariable String orderId){
//        return ordersImplementation.getOrderById(orderId);
//
//    }
}
