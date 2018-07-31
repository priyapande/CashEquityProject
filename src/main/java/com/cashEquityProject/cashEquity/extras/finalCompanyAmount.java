package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class finalCompanyAmount {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public double receivable=0;
    public double payable=0;

    public Double companyNetReceivable(String symbol) {

        String sqlBuy = "select value from orders where direction='B' and symbol=?";

        List<Order> companyReceivable = jdbcTemplate.query(sqlBuy,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        for(Order element : companyReceivable) {
            receivable=receivable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
        }
        return receivable;
    }

    public Double companyNetPayableable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and symbol=?";

        List<Order> companyPayable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        for(Order element : companyPayable) {
            payable=payable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
        }
        return payable;
    }
}
