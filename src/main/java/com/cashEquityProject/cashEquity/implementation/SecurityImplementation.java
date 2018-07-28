package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Security;
import com.cashEquityProject.cashEquity.repository.SecurityInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SecurityImplementation implements SecurityInterface {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Security> getAllSecurities(String time){

        Double price;
        List<Security> securities = jdbcTemplate.query("select * from securities",
                                                    new Object[]{},
                                                    new BeanPropertyRowMapper<>(Security.class) );

        String[] timeSplit = time.split(":", 2);

        String sql = "select price from securityModel where symbol = ? and hours = ? and time = ?";

        for (Security security: securities) {

            price = jdbcTemplate.queryForObject(sql,
                                                new Object[] {
                                                    security.getSymbol(),
                                                    timeSplit[0],
                                                    timeSplit[1]
                                                },
                                                Double.class);

            security.setPrice(price);

        }
//        Double price = jdbcTemplate.queryForObject(sql,new Object[]{
//                        security.getISIN(),
//                        timeSplit[0],
//                        timeSplit[1]},
//                Double.class );
//
//        security.setPrice(price);
        return securities;
    }

}
