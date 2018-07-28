package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.ClientInfo;
import com.cashEquityProject.cashEquity.repository.ClientInfoInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientInfoImplementation implements ClientInfoInterface {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public JSONObject userInfo(String code) {

        String sql = "select * from clientInfo where clientcode=?";

        ClientInfo client = jdbcTemplate.queryForObject(sql,
                new Object[]{code},
                new BeanPropertyRowMapper<>(ClientInfo.class));

        String name = client.getName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("name", name);

        return jsonObject;

    }
}
