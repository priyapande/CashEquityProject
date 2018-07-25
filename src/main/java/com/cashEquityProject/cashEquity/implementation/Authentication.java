package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.ClientCredentialsInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Authentication implements ClientCredentialsInterface {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void authenticate(ClientCredentials clientCredentials) {
        String authQuery = "select * from credentials where clientcode=? and ";

//        jdbcTemplate.query(authQuery, );
    }
}
