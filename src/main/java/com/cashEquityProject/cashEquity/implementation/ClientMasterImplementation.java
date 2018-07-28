package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.model.ClientMaster;
import com.cashEquityProject.cashEquity.repository.ClientMasterInterface;
import com.cashEquityProject.cashEquity.repository.config;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientMasterImplementation implements ClientMasterInterface {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public JSONObject userInfo(String code) {

        String sql = "select * from clientmaster where clientcode=?";

        ClientMaster client = jdbcTemplate.queryForObject(sql,
                new Object[]{code},
                new BeanPropertyRowMapper<>(ClientMaster.class));

        String name = client.getName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("name", name);

        return jsonObject;

    }
}
