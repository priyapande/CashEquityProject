package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Netting implements Runnable{

    @Autowired
    JdbcTemplate jdbcTemplate;

    //private char direction;
    //private String symbol;
    private Order order;

    public Netting() {

    }

    public Netting(Order order) {
        //this.direction = direction;
        //this.symbol = symbol;
        this.order = order;
    }

    @Override
    public void run() {

        String sqlBuy = "select * from orders where orderstatus in (0,1) and direction='S' and symbol=? and orderid!=? order by limitprice DESC, tradetime ASC";
        String sqlSell = "select * from orders where orderstatus in (0,1) and direction='B' and symbol=? and orderid!=? order by limitprice ASC, tradetime ASC";

        List<Order> buyNetList = jdbcTemplate.query(sqlBuy,
                new Object[]{order.getSymbol(), order.getOrderId()},
                new BeanPropertyRowMapper<>(Order.class));

        List<Order> sellNetList = jdbcTemplate.query(sqlSell,
                new Object[]{order.getSymbol(), order.getOrderId()},
                new BeanPropertyRowMapper<>(Order.class));

        //BUYORDER
        for(Order pastOrderBuy: buyNetList) {
           if (pastOrderBuy.getOrderStatus()!=2 && pastOrderBuy.getQuantity() > order.getQuantity()) {
               order.setOrderStatus(2);
               pastOrderBuy.setRemainingquantity(pastOrderBuy.getQuantity()-order.getQuantity());
               pastOrderBuy.setOrderStatus(1);
               order.setRemainingquantity(0);
               break;
           } else {
               order.setOrderStatus(1);
               order.setRemainingquantity(order.getQuantity() - pastOrderBuy.getQuantity());
               pastOrderBuy.setRemainingquantity(0);
               pastOrderBuy.setOrderStatus(2);
           }
       }

        //SELLORDER
        for(Order pastOrdersell: sellNetList) {
            if (pastOrdersell.getOrderStatus()!=2 && pastOrdersell.getQuantity() > order.getQuantity()) {
                order.setOrderStatus(2);
                pastOrdersell.setRemainingquantity(pastOrdersell.getQuantity()-order.getQuantity());
                pastOrdersell.setOrderStatus(1);
                order.setRemainingquantity(0);
                break;
            } else {
                order.setOrderStatus(1);
                order.setRemainingquantity(order.getQuantity()-pastOrdersell.getQuantity());
                pastOrdersell.setOrderStatus(2);
                pastOrdersell.setRemainingquantity(0);
            }
        }
    }
}
