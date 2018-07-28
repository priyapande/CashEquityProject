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
                        order.getValue()
                });
    }

    @Override
    public List<Order> displayOrders(String code){

        String sql = "Select * from orders where clientName in (Select name from clientMaster where clientcode=?)";

        List<Order> list = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Order.class));
        return list;
    }

    @Override
    public void deleteOrder(String orderId){

        String sql = "delete from orders where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }
}
