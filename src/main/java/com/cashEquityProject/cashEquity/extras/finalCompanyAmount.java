package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class pckCompany
{
    public double payablePck;
    public int remainingQuantityPck;
    public double receivablePck;
}

public class finalCompanyAmount {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public double receivable=0;
    public double payable=0;
    public int remainingQuantity=0;

    public pckCompany companyNetReceivable(String symbol) {

        String sqlBuy = "select value from orders where direction='B' and symbol=?";

        pckCompany objSell=new pckCompany();
        List<Order> companyReceivable = jdbcTemplate.query(sqlBuy,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        for(Order element : companyReceivable) {
            receivable=receivable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            remainingQuantity=remainingQuantity+element.getQuantity()-element.getRemainingquantity();
        }
        objSell.receivablePck=receivable;
        objSell.remainingQuantityPck=remainingQuantity;
        return objSell;
    }

    public pckCompany companyNetPayableable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and symbol=?";

        pckCompany objBuy=new pckCompany();
        List<Order> companyPayable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        for(Order element : companyPayable) {
            payable=payable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            remainingQuantity=remainingQuantity+element.getQuantity()-element.getRemainingquantity();
        }
        objBuy.payablePck=payable;
        objBuy.remainingQuantityPck=remainingQuantity;
        return objBuy;
    }
}
