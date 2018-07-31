package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class Netting implements Runnable{

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Order order;
    Logger logger = Logger.getLogger(Netting.class.getName());

    public Netting() {}

    public Netting(Order order) {
        this.order = order;
    }

    @Override
    public void run() {

        logger.info("Inside Netting run() function");

        System.out.println(order.toString());

        // String sqlBuy = "select * from orders where orderstatus in (0,1) and direction='S' and symbol=? and orderid!=? order by limitprice DESC, tradetime ASC";
        // String sqlSell = "select * from orders where orderstatus in (0,1) and direction='B' and symbol=? and orderid!=? order by limitprice ASC, tradetime ASC";

        String sqlBuy = "select * from orders where orderstatus in (0,1) and direction='S' and symbol=? and orderid!=?";
        String sqlSell = "select * from orders where orderstatus in (0,1) and direction='B' and symbol=? and orderid!=?";

        List<Order> buyNetList = jdbcTemplate.query(sqlBuy,
                new Object[]{order.getSymbol(), order.getOrderId()},
                new BeanPropertyRowMapper<>(Order.class));

        List<Order> sellNetList = jdbcTemplate.query(sqlSell,
                new Object[]{order.getSymbol(), order.getOrderId()},
                new BeanPropertyRowMapper<>(Order.class));

        // limitprice DESC, tradeprice ASC
        buyNetList.sort(new Comparator<Order>() {

            @Override
            public int compare(Order o1, Order o2) {

                // o2 first because we want to sort in DESC order
                int cmp1 = o2.getLimitPrice().compareTo(o1.getLimitPrice());

                if (cmp1 == 0) {
                    return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                }

                return cmp1;
            }
        });

        // limitprice ASC, tradeprice ASC
        sellNetList.sort(new Comparator<Order>() {

            @Override
            public int compare(Order o1, Order o2) {

                int cmp1 = o1.getLimitPrice().compareTo(o2.getLimitPrice());

                if (cmp1 == 0) {
                    return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                }

                return cmp1;
            }
        });

        // BUY ORDER
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

        // SELL ORDER
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
