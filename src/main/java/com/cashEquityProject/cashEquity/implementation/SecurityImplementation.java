package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.model.Security;
import com.cashEquityProject.cashEquity.model.SecurityModel;
import com.cashEquityProject.cashEquity.repository.SecurityInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

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

        // List of security symbols.
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

    @Override
    public List<Security> getTopSecuritiesByCount(String date, String time) {
        /*
         * Get top buy and sell securities from MYSQL table based on buy and sell count.
         */

        // Top 5 Buy Securities
        String sql1 = "select * from securities order by buycount DESC limit 5";

        List<Security> securityList = jdbcTemplate.query(sql1,
                new Object[]{},
                new BeanPropertyRowMapper<>(Security.class));

        // Top 5 Sell Securities
        String sql2 = "select * from securities order by sellcount DESC limit 5";

        securityList.addAll(jdbcTemplate.query(sql2,
                new Object[]{},
                new BeanPropertyRowMapper<>(Security.class)));

        return securityList;
    }

    @Override
    public String getTopSecuritiesByPrice(String date, String time) {
        /*
         * Get top buy and sell securities from MYSQL table based on price.
         */

        String[] currentTimeFields = time.split(":");
        Integer currHour = Integer.parseInt(currentTimeFields[0]);
        Integer currMin = Integer.parseInt(currentTimeFields[1]);

        String query = "select * from securityprice where date = ?";

        JSONArray result = new JSONArray();

        List<SecurityModel> securities = jdbcTemplate.query(query,
                new Object[]{date},
                new BeanPropertyRowMapper<>(SecurityModel.class));

        Integer hour, minute;

        // Filter securities based on time
        Iterator<SecurityModel> iterator = securities.iterator();
        while (iterator.hasNext()) {

            String[] timeFields = iterator.next().getTime().split(":");
            hour = Integer.parseInt(timeFields[0]);
            minute = Integer.parseInt(timeFields[1]);

            if (!((currHour > hour) || ((currHour.equals(hour) && currMin > minute)))) {
                iterator.remove();
            }

        }

        securities.sort(new Comparator<SecurityModel>() {
            @Override
            public int compare(SecurityModel o1, SecurityModel o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });

        Integer len = securities.size();

        List<SecurityModel> buyList = securities.subList(len-5, len);
        List<SecurityModel> sellList = securities.subList(0, 5);

        JSONArray buyJSON = new JSONArray();
        for (SecurityModel x: buyList) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("symbol", x.getSymbol());
            jsonObject.put("price", x.getPrice());

            buyJSON.put(jsonObject);
        }

        result.put(buyJSON);

        JSONArray sellJSON = new JSONArray();
        for (SecurityModel x: sellList) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("symbol", x.getSymbol());
            jsonObject.put("price", x.getPrice());

            sellJSON.put(jsonObject);

        }

        result.put(sellJSON);

        return result.toString(4);
    }
}
