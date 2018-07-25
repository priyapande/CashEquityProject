package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.ClientCredentialsInterface;
import com.cashEquityProject.cashEquity.repository.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Authentication implements ClientCredentialsInterface, config {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer authenticate(ClientCredentials clientCredentials) {
        String authQuery = "select * from credentials where clientcode=?";

        String clientcode = clientCredentials.getClientCode();
        String inpPassword = clientCredentials.getPassword();

        ClientCredentials selectedUser = jdbcTemplate.queryForObject(authQuery,
                new Object[]{clientcode, inpPassword},
                new BeanPropertyRowMapper<>(ClientCredentials.class));

        if (selectedUser != null) {
            return config.INVALID_USER;
        } else if (selectedUser.getPassword().equals(inpPassword)){
            return config.SUCCESS;
        } else if (!selectedUser.getPassword().equals(inpPassword)){
            return config.INVALID_PASSWORD;
        } else {
            return config.FAILED;
        }
    }
}
