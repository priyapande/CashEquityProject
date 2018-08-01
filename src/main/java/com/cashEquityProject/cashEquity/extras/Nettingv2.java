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

        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
        }

        String sql = "select * from orders where direction != ? and symbol = ? and orderid != ? and limitprice <= ? order by limitprice " + orderBy + ", tradetime ASC";

        try {

            selectedOrders = jdbcTemplate.query(sql,
                    new Object[]{order.getDirection(), order.getSymbol(), order.getOrderId(), order.getLimitPrice()},
                    new BeanPropertyRowMapper<>(Order.class));

            selectedOrders = executeTrade(selectedOrders);
            updateTable(selectedOrders);

        } catch (DataAccessException exp) {

            // This means, there is no order in the market right now to match with the new order.

        }


    }

    private JSONArray runMarketOrder() {

        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
        }

        String sql = "select * from orders where direction != ? and symbol = ? and orderid != ? order by limitprice " + orderBy + ", tradetime ASC";

        selectedOrders = (List<Order>) (Object)jdbcTemplate.queryForList(sql,
                                            new Object[]{order.getDirection(), order.getSymbol(), order.getOrderId()},
                                            new BeanPropertyRowMapper<>(Order.class));

        if (selectedOrders.size() == 0) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", config.FAILED);
            jsonObject.put("msg", "No match found for this market order");
            jsonArray.put(jsonObject);

        } else {

            selectedOrders = executeTrade(selectedOrders);
            updateTable(selectedOrders);

        }

        return jsonArray;

    }

    private List<Order> executeTrade(List<Order> orders) {

        int rowRemaining, remaining;

        // NOTE: matches for order(newly inserted) will be ''.
        JSONArray rowJSONArray;
        JSONArray orderJSONArray = new JSONArray();

        for (Order rowOrder: orders) {

            rowJSONArray = new JSONArray(rowOrder.getMatches());

            JSONObject jsonObject = new JSONObject();   // for order (newly inserted)
            JSONObject rowJSON = new JSONObject();      // for row order

            rowRemaining = rowOrder.getRemainingquantity();
            remaining = order.getRemainingquantity();

            if (rowRemaining > remaining) {

                rowOrder.setRemainingquantity(rowRemaining - remaining);
                rowOrder.setOrderStatus(1);

                rowJSON.put("orderid", order.getOrderId());
                rowJSON.put("price", rowOrder.getLimitPrice());
                rowJSON.put("quantity", remaining);

                order.setRemainingquantity(0);
                order.setOrderStatus(2);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", remaining);

            } else if (rowRemaining < remaining) {

                rowOrder.setRemainingquantity(0);
                rowOrder.setOrderStatus(2);

                rowJSON.put("orderid", order.getOrderId());
                rowJSON.put("price", rowOrder.getLimitPrice());
                rowJSON.put("quantity", rowRemaining);

                order.setRemainingquantity(remaining - rowRemaining);
                order.setOrderStatus(1);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", rowRemaining);


            } else {

                rowOrder.setRemainingquantity(0);
                rowOrder.setOrderStatus(2);

                rowJSON.put("orderid", order.getOrderId());
                rowJSON.put("price", rowOrder.getLimitPrice());
                rowJSON.put("quantity", remaining); // or rowRemaining

                order.setRemainingquantity(0);
                order.setOrderStatus(2);

                jsonObject.put("orderid", rowOrder.getOrderId());
                jsonObject.put("price", rowOrder.getLimitPrice());
                jsonObject.put("quantity", remaining); // or rowRemaining

            }

            rowJSONArray.put(rowJSON); // Add new match entry for roworder
            rowOrder.setMatches(rowJSONArray.toString());

            orderJSONArray.put(jsonObject); // Add new match for order

            if (order.getOrderStatus() == 2) {
                break;
            }

        }

        // Set Matches JSON array string to order (newly inserted).
        order.setMatches(orderJSONArray.toString());

        if (order.getTradeType().toLowerCase().equals("market")) {

            if (order.getRemainingquantity() > 0) {

                order.setLimitPrice();

            }

        }

        return orders;

    }

    private void updateTable(List<Order> selectedOrders) {

        String sql = "update orders (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) set " +
                    "clientCode=?, symbol=?, tradedate=?, tradetime=?, quantity=?, tradetype=?, limitprice=?, direction=?, value=?, orderstatus=?, remainingquantity=?, matches=? " +
                    "where orderid = ?";

        int n = selectedOrders.size();

        for (int i = 0; i < n ; i++) {

            Order rowOrder = selectedOrders.get(i);

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
                            Types.INTEGER,   // remaining quantity,
                            Types.VARCHAR,   // Match JSON string
                            Types.INTEGER    // Order id
                    });
        }

    }

}































