package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class finalClientAmount {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public double receivable=0;
    public double payable=0;

    public Double clientNetPayable(String code) {

        String sqlBuy = "select value from orders where direction='B' and clientCode=?";

        List<Order> clientPayable = jdbcTemplate.query(sqlBuy,
                new Object[]{code},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientPayable)
        {
            payable=payable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
        }
        return payable;
    }

    public Double clientNetReceivable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and clientCode=?";

        List<Order> clientReceivable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientReceivable)
        {
            receivable=receivable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
        }
        return receivable;
    }
}
