package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.ClientCredentialsInterface;
import com.cashEquityProject.cashEquity.repository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class Authentication implements ClientCredentialsInterface, config {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = Logger.getLogger(Authentication.class.getName());

    @Override
    public Integer authenticate(ClientCredentials clientCredentials) {
        String authQuery = "select * from credentials where clientCode=?";

        String clientcode = clientCredentials.getClientCode();
        String inpPassword = clientCredentials.getPassword();

        try{

            ClientCredentials selectedUser = jdbcTemplate.queryForObject(authQuery,
                    new Object[]{clientcode},
                    new BeanPropertyRowMapper<>(ClientCredentials.class));

            if (selectedUser.getPassword().equals(inpPassword)){
                return config.SUCCESS;
            } else {
                return config.INVALID_PASSWORD;
            }

        } catch (EmptyResultDataAccessException exp) {

            return config.INVALID_USER;

        } catch (Exception exp) {
            System.out.println("Server problem");
            return 10000;
        }

    }
}
