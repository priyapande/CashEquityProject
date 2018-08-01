package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FinalClientAmount {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private double receivable;
    private double payable;
    private int processedQuantity;

    public FinalClientAmount() {
        this.receivable = 0;
        this.payable = 0;
        this.processedQuantity = 0;
    }

    public Report clientNetPayable(String code) {

        String sqlBuy = "select value from orders where direction='B' and clientCode=?";

        Report objBuy = new Report();
        List<Order> clientPayable = jdbcTemplate.query(sqlBuy,
                new Object[]{code},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientPayable)
        {
            payable += element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            processedQuantity += element.getQuantity()-element.getRemainingquantity();
        }

        objBuy.setPayable(payable);
        objBuy.setProcessedQuantity(processedQuantity);
        return objBuy;
    }

    public Report clientNetReceivable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and clientCode=?";

        Report objSell=new Report();
        List<Order> clientReceivable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));
        for(Order element : clientReceivable)
        {
            receivable += element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            processedQuantity += element.getQuantity()-element.getRemainingquantity();
        }

        objSell.setReceivable(receivable);
        objSell.setProcessedQuantity(processedQuantity);

        return objSell;
    }
}