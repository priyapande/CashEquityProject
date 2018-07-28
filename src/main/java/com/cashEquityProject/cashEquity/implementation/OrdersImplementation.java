package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.SecurityMaster;
import com.cashEquityProject.cashEquity.repository.OrdersInterface;

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

        String sql = "insert into order (clientcode, security, tradedate, tradetime, quantity, tradetype, limitprice, direction, value) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
    public List<Order> getOrders(String code){
        /*
         * Get client orders from MYSQL table using client code.
         * Args:
         *  code : Client code.
         */

        String sql = "select * from orders where orderId = (select orderId from clientOrder where clientCode=?)";

        return jdbcTemplate.query(sql,
                                new Object[]{code},
                                new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public SecurityMaster displaySecurity(String time){

        String sql = "Select * from securityMaster";

        SecurityMaster securityMaster = jdbcTemplate.queryForObject(sql,
                new Object[]{}, new BeanPropertyRowMapper<>(SecurityMaster.class) );

        String [] timeSplit = time.split(":", 2);

        String sql2 = "Select price from securityModel where isinNo = ? and hours = ? and time = ?";

        Double price = jdbcTemplate.queryForObject(sql,new Object[]{
                securityMaster.getIsin(),
                timeSplit[0],
                timeSplit[1]},
                Double.class );

        securityMaster.setPrice(price);
        return securityMaster;
    }

    @Override
    public void deleteOrder(String orderId){
        /*
         * Delete client order from MYSQL table using order id.
         * Args:
         *  orderId : Order Id.
         */

        String sql = "delete from orders where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }
}
