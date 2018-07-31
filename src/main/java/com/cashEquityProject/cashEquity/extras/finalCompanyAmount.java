package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class finalCompanyAmount {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private double receivable;
    private double payable;
    private int processedQuantity;

    public finalCompanyAmount() {
        this.receivable = 0;
        this.payable = 0;
        this.processedQuantity = 0;
    }

    public Report companyNetReceivable(String symbol) {

        String sqlBuy = "select value from orders where direction='B' and symbol=?";

        Report objSell=new Report();
        List<Order> companyReceivable = jdbcTemplate.query(sqlBuy,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        receivable = processedQuantity = 0;
        for(Order element : companyReceivable) {
            receivable += element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            processedQuantity += element.getQuantity()-element.getRemainingquantity();
        }

        objSell.setReceivable(receivable);
        objSell.setProcessedQuantity(processedQuantity);

        return objSell;
    }

    public Report companyNetPayableable(String symbol) {


        String sqlSell = "select value from orders where direction='S' and symbol=?";

        Report objBuy = new Report();
        List<Order> companyPayable = jdbcTemplate.query(sqlSell,
                new Object[]{symbol},
                new BeanPropertyRowMapper<>(Order.class));

        payable = processedQuantity = 0;
        for(Order element : companyPayable) {
            payable += element.getLimitPrice()*(element.getQuantity()-element.getRemainingquantity());
            processedQuantity += element.getQuantity()-element.getRemainingquantity();
        }

        objBuy.setPayable(payable);
        objBuy.setProcessedQuantity(processedQuantity);

        return objBuy;
    }
}
