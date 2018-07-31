package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.repository.config;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

public class Nettingv2 implements Runnable{

    private JdbcTemplate jdbcTemplate;

    private Order order;
    private Logger logger = Logger.getLogger(Netting.class.getName());

    public Nettingv2() {}

    public Nettingv2(Order order, JdbcTemplate jdbcTemplate) {

        this.order = order;
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void run() {

        if (order.getTradeType().toLowerCase().equals("limit")) {
            runMarketOrder();
        } else {
            runLimitOrder();
        }

    }

    private void runLimitOrder() {

        TradeInfo tradeInfo;
        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
        }

        String sql = "select * from orders where direction != ? and symbol = ? and limitprice <= ? order by limitprice " + orderBy + ", tradetime ASC";

        try {

            selectedOrders = jdbcTemplate.query(sql,
                    new Object[]{order.getDirection(), order.getSymbol(), order.getLimitPrice()},
                    new BeanPropertyRowMapper<>(Order.class));

            tradeInfo = executeTrade(selectedOrders, jsonArray);
            updateTable(tradeInfo);

        } catch (DataAccessException exp) {

            // This means, there is no order in the market right now to match with the new order.

        }


    }

    private JSONArray runMarketOrder() {

        TradeInfo tradeInfo;
        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
        }

        String sql = "select * from orders where direction != ? and symbol = ? order by limitprice " + orderBy + ", tradetime ASC";

        try {

            selectedOrders = jdbcTemplate.query(sql,
                    new Object[]{order.getDirection(), order.getSymbol()},
                    new BeanPropertyRowMapper<>(Order.class));

            tradeInfo = executeTrade(selectedOrders, jsonArray);
            updateTable(tradeInfo);

        } catch (DataAccessException exp) {
            // TODO: FIND CORRECT Exception

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", config.FAILED);
            jsonObject.put("msg", "No match found for this market order");
            jsonArray.put(jsonObject);

        }

        return jsonArray;

    }

    private TradeInfo executeTrade(List<Order> orders, JSONArray jsonArray) {

        int rowRemaining, remaining;

        for (Order rowOrder: orders) {

            JSONObject jsonObject = new JSONObject();

            rowRemaining = rowOrder.getRemainingquantity();
            remaining = order.getRemainingquantity();

            if (rowRemaining > remaining) {

                rowOrder.setRemainingquantity(rowRemaining - remaining);
                rowOrder.setOrderStatus(1);

                order.setRemainingquantity(0);
                order.setOrderStatus(2);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", order.getRemainingquantity());


            } else if (rowRemaining < remaining) {

                rowOrder.setRemainingquantity(0);
                rowOrder.setOrderStatus(2);

                order.setRemainingquantity(remaining - rowRemaining);
                order.setOrderStatus(1);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", rowRemaining);


            } else {

                rowOrder.setRemainingquantity(0);
                rowOrder.setOrderStatus(2);

                order.setRemainingquantity(0);
                order.setOrderStatus(2);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", remaining);

            }

            jsonArray.put(jsonObject);

            if (order.getOrderStatus() == 2) {
                break;
            }

        }

        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setJsonArray(jsonArray);
        tradeInfo.setOrders(orders);

        return tradeInfo;

    }

    private void updateTable(TradeInfo tradeInfo) {

        List<Order> orders = tradeInfo.getOrders();
        String matches = tradeInfo.getJsonArray().toString();

        String sql = "update orders (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) set " +
                    "clientCode=?, symbol=?, tradedate=?, tradetime=?, quantity=?, tradetype=?, limitprice=?, direction=?, value=?, orderstatus=?, remainingquantity=?, matches=? " +
                    "where orderid = ?";

        for (Order rowOrder: orders) {

            jdbcTemplate.update(sql,
                    rowOrder.getClientCode(),
                    rowOrder.getSymbol(),
                    rowOrder.getTradedate(),
                    rowOrder.getTradetime(),
                    rowOrder.getQuantity(),
                    rowOrder.getTradeType(),
                    rowOrder.getLimitPrice(),
                    rowOrder.getDirection(),
                    rowOrder.getValue(),
                    rowOrder.getOrderStatus(),
                    rowOrder.getRemainingquantity(),
                    rowOrder.getMatches(),
                    rowOrder.getOrderId(),
                    new int[]{Types.VARCHAR, // client code
                            Types.VARCHAR,   // security symbol
                            Types.VARCHAR,   // trade date
                            Types.VARCHAR,   // trade time
                            Types.INTEGER,   // quantity
                            Types.VARCHAR,   // trade type
                            Types.FLOAT,     // limit price
                            Types.CHAR,      // direction
                            Types.FLOAT,     // value
                            Types.INTEGER,   // order status
                            Types.INTEGER,   // remaining quantity);,
                            Types.VARCHAR,
                            Types.INTEGER    // Order id
                    });
        }

    }

}































