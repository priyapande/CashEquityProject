package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
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

    private JdbcTemplate jdbcTemplate;

    private Order order;
    private Logger logger = Logger.getLogger(Netting.class.getName());

    public Netting() {}

    public Netting(Order order, JdbcTemplate jdbcTemplate) {
        this.order = order;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run() {

        String sql = "select * from orders where orderStatus in (0, 1) and direction != ? and symbol = ? and orderId != ?";

        List<Order> orderList = jdbcTemplate.query(sql,
                                                    new Object[]{order.getDirection(), order.getSymbol(), order.getOrderId()},
                                                    new BeanPropertyRowMapper<>(Order.class));

        orderList.sort(new Comparator<Order>() {

            Character direction = order.getDirection();

            @Override
            public int compare(Order o1, Order o2) {

                int cmp1;

                if (direction.equals('B')){
                    // For buy orders, best orders are
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

        int pastRemaining, orderRemaining;

        for(Order pastOrderBuy: orderList) {

            // Do not process executed orders.
            if (pastOrderBuy.getOrderStatus() == 2) {
                continue;
            }

            pastRemaining = pastOrderBuy.getRemainingquantity();
            orderRemaining = order.getRemainingquantity();

            if (pastRemaining > orderRemaining) {
                // Past order : partially executed
                pastOrderBuy.setRemainingquantity(pastRemaining - orderRemaining);
                pastOrderBuy.setOrderStatus(1);

                // Mark current order as executed and break from loop
                order.setRemainingquantity(0);
                order.setOrderStatus(2);
                break;

            } else if (pastRemaining < orderRemaining){

                // Mark current order as partially executed
                order.setOrderStatus(1);
                order.setRemainingquantity(orderRemaining - pastRemaining);

                // Mark past order as executed
                pastOrderBuy.setRemainingquantity(0);
                pastOrderBuy.setOrderStatus(2);

            } else {

                // Mark past order as completed
                pastOrderBuy.setRemainingquantity(0);
                pastOrderBuy.setOrderStatus(2);

                // Mark current order as completed
                order.setRemainingquantity(0);
                order.setOrderStatus(2);
            }
        }

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
