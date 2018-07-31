package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class Netting implements Runnable{

    JdbcTemplate jdbcTemplate;

    private Order order;
    Logger logger = Logger.getLogger(Netting.class.getName());

    public Netting() {}

    public Netting(Order order, JdbcTemplate jdbcTemplate) {
        this.order = order;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run() {

        logger.info("Inside Netting run() function");

        System.out.println(order.toString());

//        String sqlBuy = "select * from orders where orderStatus in (0,1) and direction='S' and symbol=? and orderId!=?";
//        String sqlSell = "select * from orders where orderStatus in (0,1) and direction='B' and symbol=? and orderId!=?";

        String sql = "select * from orders where orderStatus in (0, 1) and direction != ? and symbol = ? and orderId != ?";

        List<Order> orderList = jdbcTemplate.query(sql,
                                                    new Object[]{order.getDirection(), order.getSymbol(), order.getOrderId()},
                                                    new BeanPropertyRowMapper<>(Order.class));

//        List<Order> buyNetList = jdbcTemplate.query(sqlBuy,
//                new Object[]{order.getSymbol(), order.getOrderId()},
//                new BeanPropertyRowMapper<>(Order.class));
//
//        List<Order> sellNetList = jdbcTemplate.query(sqlSell,
//                new Object[]{order.getSymbol(), order.getOrderId()},
//                new BeanPropertyRowMapper<>(Order.class));

        orderList.sort(new Comparator<Order>() {

            Character direction = order.getDirection();

            @Override
            public int compare(Order o1, Order o2) {

                int cmp1;

                if (direction.equals('B')){
                    cmp1 = o2.getLimitPrice().compareTo(o1.getLimitPrice());
                } else {
                    cmp1 = o1.getLimitPrice().compareTo(o2.getLimitPrice());
                }

                if (cmp1 == 0) {
                    return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                }

                return cmp1;
            }
        });

//        // limitprice DESC, tradeprice ASC
//        buyNetList.sort(new Comparator<Order>() {
//
//            @Override
//            public int compare(Order o1, Order o2) {
//
//                // o2 first because we want to sort in DESC order
//                int cmp1 = o2.getLimitPrice().compareTo(o1.getLimitPrice());
//
//                if (cmp1 == 0) {
//                    return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
//                }
//
//                return cmp1;
//            }
//        });
//
//        // limitprice ASC, tradeprice ASC
//        sellNetList.sort(new Comparator<Order>() {
//
//            @Override
//            public int compare(Order o1, Order o2) {
//
//                int cmp1 = o1.getLimitPrice().compareTo(o2.getLimitPrice());
//
//                if (cmp1 == 0) {
//                    return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
//                }
//
//                return cmp1;
//            }
//        });

        for(Order pastOrderBuy: orderList) {
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

//        // BUY ORDER
//        for(Order pastOrderBuy: buyNetList) {
//           if (pastOrderBuy.getOrderStatus()!=2 && pastOrderBuy.getQuantity() > order.getQuantity()) {
//               order.setOrderStatus(2);
//               pastOrderBuy.setRemainingquantity(pastOrderBuy.getQuantity()-order.getQuantity());
//               pastOrderBuy.setOrderStatus(1);
//               order.setRemainingquantity(0);
//               break;
//           } else {
//               order.setOrderStatus(1);
//               order.setRemainingquantity(order.getQuantity() - pastOrderBuy.getQuantity());
//               pastOrderBuy.setRemainingquantity(0);
//               pastOrderBuy.setOrderStatus(2);
//           }
//       }
//
//        // SELL ORDER
//        for(Order pastOrdersell: sellNetList) {
//            if (pastOrdersell.getOrderStatus()!=2 && pastOrdersell.getQuantity() > order.getQuantity()) {
//                order.setOrderStatus(2);
//                pastOrdersell.setRemainingquantity(pastOrdersell.getQuantity()-order.getQuantity());
//                pastOrdersell.setOrderStatus(1);
//                order.setRemainingquantity(0);
//                break;
//            } else {
//                order.setOrderStatus(1);
//                order.setRemainingquantity(order.getQuantity()-pastOrdersell.getQuantity());
//                pastOrdersell.setOrderStatus(2);
//                pastOrdersell.setRemainingquantity(0);
//            }
//        }

//        List<Order> updatedOrders = new ArrayList<Order>();
//        updatedOrders.addAll(buyNetList);
//        updatedOrders.addAll(sellNetList);

        // Update orders
        String updateQuery = "update orders set remainingquantity=?, orderstatus=? where orderid=?";

        for (Order updatedOrder: orderList) {
            jdbcTemplate.update(updateQuery,
                                new Object[]{updatedOrder.getRemainingquantity(), updatedOrder.getOrderStatus(), updatedOrder.getOrderId()},
                                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});
        }

        jdbcTemplate.update(updateQuery,
                new Object[]{order.getRemainingquantity(), order.getOrderStatus(), order.getOrderId()},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});

        logger.info("Netting done!");

    }
}
