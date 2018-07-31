package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class pck
{
    public double payablePck;
    public int remainingQuantityPck;
    public double receivablePck;
}

public class finalClientAmount {
    @Autowired
    JdbcTemplate jdbcTemplate;


    public double receivable=0;
    public double payable=0;
    public int remainingQuantity=0;

    public pck clientNetPayable(String code) {

        String sqlBuy = "select value from orders where direction='B' and clientCode=?";

        pck objBuy=new pck();
        List<Order> clientPayable = jdbcTemplate.query(sqlBuy,
                new Object[]{code},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientPayable)
        {
            payable=payable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            remainingQuantity=remainingQuantity+element.getQuantity()-element.getRemainingquantity();
        }
        objBuy.payablePck=payable;
        objBuy.remainingQuantityPck=remainingQuantity;
        return objBuy;
    }

    public pck clientNetReceivable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and clientCode=?";

        pck objSell=new pck();
        List<Order> clientReceivable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientReceivable)
        {
            receivable=receivable + element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            remainingQuantity=remainingQuantity+element.getQuantity()-element.getRemainingquantity();
        }
        objSell.receivablePck=receivable;
        objSell.remainingQuantityPck=remainingQuantity;
        return objSell;
    }
}
