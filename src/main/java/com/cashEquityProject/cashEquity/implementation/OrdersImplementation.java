package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.repository.OrdersInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;


@Repository
public class OrdersImplementation implements OrdersInterface {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void addOrder(Order order){

        String sql = "insert into order values(?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(sql,
                new Object[]{
                        order.getClientName(),
                        order.getSecurity(),
                        order.getDate(),
                        order.getTime(),
                        order.getQuantity(),
                        order.getTradeType(),
                        order.getLimitPrice(),
                        order.getDirection(),
                        order.getValue(),
                        order.getOrderId()
                });
    }

    @Override
    public List<Order> displayOrders(String code){

        System.out.println("CODE: " + code);
        String sql = "select * from orders where orderid = (select orderid from clientorder where clientcode=?)";

        return jdbcTemplate.query(sql,
                new Object[]{code},
                new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public void deleteOrder(String orderId){

        String sql = "delete from orders where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }
}
