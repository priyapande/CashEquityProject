package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.ClientCredentialsInterface;
import com.cashEquityProject.cashEquity.repository.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        String authQuery = "select * from credentials where clientCode=?";

        String clientcode = clientCredentials.getClientCode();
        String inpPassword = clientCredentials.getPassword();

<<<<<<< HEAD
        System.out.println(clientcode + " " + inpPassword);

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
=======
        System.out.println(clientcode + inpPassword);
        ClientCredentials selectedUser = jdbcTemplate.queryForObject(authQuery,
                new Object[]{clientcode, inpPassword},
                new BeanPropertyRowMapper<>(ClientCredentials.class));

        if (selectedUser == null) {
>>>>>>> 4a56302ef08e5fd6e95591370473f24ef7cb96a2
            return config.INVALID_USER;
        }

    }
}
