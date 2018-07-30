package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Security;
import com.cashEquityProject.cashEquity.model.SecurityModel;
import com.cashEquityProject.cashEquity.repository.SecurityInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class SecurityImplementation implements SecurityInterface {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Security> getAllSecurities(String date, String time){

        Double price;
        List<Security> securities = jdbcTemplate.query("select * from securities",
                                                    new Object[]{},
                                                    new BeanPropertyRowMapper<>(Security.class));


        List<String> symbols = new ArrayList<String>();
        for (Security security: securities) {
            symbols.add(security.getSymbol());
        }

        String sql = "select * from securityprice where symbol in ('" + String.join("','", symbols) + "') and date = ? and time = ?";
        List<SecurityModel> prices = jdbcTemplate.query(sql,
                                                new Object[]{date, time},
                                                new BeanPropertyRowMapper<>(SecurityModel.class));

        for (int i=0; i<prices.size(); i++) {
            securities.get(i).setPrice(prices.get(i).getPrice());
        }

        return securities;
    }

}
