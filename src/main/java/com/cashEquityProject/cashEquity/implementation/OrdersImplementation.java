package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.Security;
import com.cashEquityProject.cashEquity.repository.OrdersInterface;

import com.cashEquityProject.cashEquity.repository.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;


@Repository
public class OrdersImplementation implements OrdersInterface {

    /*
     * Repository class handling OrdersInterface.
     * Implements functions to add, get and delete orders.
     */

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addOrder(Order order){
        /*
         * Add client order to MYSQL table using Order object.
         * Args:
         *  order : Order object.
         */

        String sqlUpdateCount;

        // Insert command (no orderId because it is auto incremented by MySQL)
        String sql = "insert into orders" +
                    " (clientcode, security, tradedate, tradetime, quantity, tradetype, limitprice, direction, value, orderstatus)" +
                    " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                new Object[]{
                        order.getClientCode(),
                        order.getSymbol(),
                        order.getTradedate(),
                        order.getTradetime(),
                        order.getQuantity(),
                        order.getTradeType(),
                        order.getLimitPrice(),
                        order.getDirection(),
                        order.getValue(),
                        order.getOrderStatus()
                },
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.CHAR, Types.FLOAT, Types.INTEGER});

        if(order.getOrderStatus() == 'B')
            sqlUpdateCount = "update orders set buycount = buycount + 1 where orderid = ?";
        else
            sqlUpdateCount = "update orders set sellcount = sellcount + 1 where orderid = ?";

        jdbcTemplate.update(sqlUpdateCount,
                new Object[]{order.getOrderId()},
                new int[]{Types.VARCHAR});
    }

    @Override
    public List<Order> getOrders(String code){
        /*
         * Get client orders from MYSQL table using client code.
         * Args:
         *  code : Client code.
         */

        String sql = "select * from orders where clientCode = ?";

        return jdbcTemplate.query(sql,
                                new Object[]{code},
                                new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public void deleteOrder(String orderId){
        /*
         * Delete client order from MYSQL table using order id.
         * Args:
         *  orderId : Order Id.
         */

        String sql = "update orders set orderstatus = " + config.EXECUTED  + " where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }

    @Override
    public void cancelOrder(String orderId){
        /*
         * For orders that have been cancelled by the client
         * Args:
         *  orderId : Order Id.
         */

        String sql = "update orders set orderstatus = " + config.ORDER_CANCELLED  + " where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }

    @Override
    public List<Order> getTopOrders(String symbol){
        /*
         * Get top buy and sell orders from MYSQL table using security symbol.
         * Args:
         *  code : security symbol.
         */

        // TODO: Date and time for selection of orders.
        // TODO: Consider pricevariancelimit

        // Top 5 Buy Orders
        String sql1 = "select * from orders where symbol = ? and direction = 'B' and orderstatus in (0, 1) order by limitprice DESC limit 5";

        List<Order> securityList = jdbcTemplate.query(sql1,
                                         new Object[]{symbol},
                                         new BeanPropertyRowMapper<>(Order.class));

        // Top 5 Sell Orders
        String sql2 = "select * from orders where symbol = ? and direction = 'S' and orderstatus in (0, 1) order by limitprice ASC limit 5";

        securityList.addAll(jdbcTemplate.query(sql2,
                            new Object[]{symbol},
                            new BeanPropertyRowMapper<>(Order.class)));

        return securityList;
    }
}
