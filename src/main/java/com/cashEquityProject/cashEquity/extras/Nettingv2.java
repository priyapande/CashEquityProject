package com.cashEquityProject.cashEquity.extras;

import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.repository.config;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Nettingv2{

    private JdbcTemplate jdbcTemplate;

    private Order order;
    private Logger logger = Logger.getLogger(Netting.class.getName());

    public Nettingv2() {}

    public Nettingv2(Order order, JdbcTemplate jdbcTemplate) {

        this.order = order;
        this.jdbcTemplate = jdbcTemplate;

    }

    public void run() {

        if (order.getTradeType().toLowerCase().equals("market")) {
            runMarketOrder();
        } else {
            runLimitOrder();
        }

    }

    private void runLimitOrder() {

        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        String dir = "S";
        String comp = "<=";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
            comp = ">=";
            dir = "B";
        }


        String sql = "select * from orders where direction = '" + dir + "' and symbol = ? and orderid != ? and limitprice " + comp + " ?";

        selectedOrders = jdbcTemplate.query(sql,
                    new Object[]{order.getSymbol(), order.getOrderId(), order.getLimitPrice()},
                    new BeanPropertyRowMapper<>(Order.class));

        // Equivalent to order by tradetime ASC
        if (order.getDirection().equals('B')) {
            selectedOrders.sort(new Comparator<Order>() {

                @Override
                public int compare(Order o1, Order o2) {

                    int cmp1 = o1.getLimitPrice().compareTo(o2.getLimitPrice());
                    if (cmp1 == 0) {
                        return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                    }
                    return cmp1;
                }

            });
        } else {
            selectedOrders.sort(new Comparator<Order>() {

                @Override
                public int compare(Order o1, Order o2) {

                    int cmp1 = o2.getLimitPrice().compareTo(o1.getLimitPrice());
                    if (cmp1 == 0) {
                        return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                    }
                    return cmp1;
                }

            });
        }

        selectedOrders = executeTrade(selectedOrders);
        updateTable(selectedOrders);

    }

    private JSONArray runMarketOrder() {

        List<Order> selectedOrders;
        JSONArray jsonArray = new JSONArray();

        String orderBy = "ASC";
        String dir = "S";
        if (order.getDirection().equals('S')) {
            orderBy = "DESC";
            dir = "B";
        }

        String sql = "select * from orders where direction = '" + dir + "' and symbol = ? and orderid != ?";

        selectedOrders = (List<Order>) (Object) jdbcTemplate.queryForList(sql,
                                            new Object[]{order.getSymbol(), order.getOrderId()},
                                            new BeanPropertyRowMapper<>(Order.class));

        // Equivalent to order by tradetime ASC
        if (order.getDirection().equals('B')) {
            selectedOrders.sort(new Comparator<Order>() {

                @Override
                public int compare(Order o1, Order o2) {

                    int cmp1 = o1.getLimitPrice().compareTo(o2.getLimitPrice());
                    if (cmp1 == 0) {
                        return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                    }
                    return cmp1;
                }

            });
        } else {
            selectedOrders.sort(new Comparator<Order>() {

                @Override
                public int compare(Order o1, Order o2) {

                    int cmp1 = o2.getLimitPrice().compareTo(o1.getLimitPrice());
                    if (cmp1 == 0) {
                        return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
                    }
                    return cmp1;
                }

            });
        }

//        // Equivalent to order by tradetime ASC
//        selectedOrders.sort(new Comparator<Order>() {
//            @Override
//            public int compare(Order o1, Order o2) {
//                return TimeComparator.compare(o1.getTradetime(), o2.getTradetime());
//            }
//        });

        if (selectedOrders.size() == 0) {

            // TODO: Confirm this action.

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
        Double lastPriceMatch = -1.0;

        // NOTE: matches for order(newly inserted) will be ''.
        JSONArray rowJSONArray;
        JSONArray orderJSONArray = new JSONArray();

        for (Order rowOrder: orders) {

            if (order.getOrderStatus() == 2) {
                break;
            }

            if (rowOrder.getMatches().equals("")) {
                rowJSONArray = new JSONArray();
            } else {
                rowJSONArray = new JSONArray(rowOrder.getMatches());
            }

            JSONObject jsonObject = new JSONObject();   // for order (newly inserted)
            JSONObject rowJSON = new JSONObject();      // for row order

            rowRemaining = rowOrder.getRemainingquantity();
            if (rowRemaining == 0) {
                continue;
            }

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

                lastPriceMatch = rowOrder.getLimitPrice();

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

                lastPriceMatch = rowOrder.getLimitPrice();

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

                lastPriceMatch = rowOrder.getLimitPrice();

            }

            rowJSON.put("client", order.getClientCode());
            jsonObject.put("client", rowOrder.getClientCode());

            rowJSONArray.put(rowJSON); // Add new match entry for roworder
            rowOrder.setMatches(rowJSONArray.toString());

            orderJSONArray.put(jsonObject); // Add new match for order

        }

        // Set Matches JSON array string to order (newly inserted).
        order.setMatches(orderJSONArray.toString());

        // For partially executed market order, set LimitPrice to last matched price.
        // This is done because, a partially executed market order has to be put to order book, so it must carry a price.
        if (order.getTradeType().toLowerCase().equals("market")) {
            if (order.getRemainingquantity() > 0) {
                order.setLimitPrice(lastPriceMatch);
            }
        }

        return orders;

    }

    private void updateTable(List<Order> selectedOrders) {

        String sql = "update orders set " +
                    "limitprice=?, orderstatus=?, remainingquantity=?, matches=? " +
                    "where orderid = ?";

        selectedOrders.add(order);

        for (Order rowOrder: selectedOrders) {

            // TODO: What to put in Value field??

            jdbcTemplate.update(sql,
                    new Object[]{
                                rowOrder.getLimitPrice(),
                                rowOrder.getOrderStatus(),
                                rowOrder.getRemainingquantity(),
                                rowOrder.getMatches(),
                                rowOrder.getOrderId(),
                    },
                    new int[]{
                            Types.FLOAT,     // limit price
                            Types.INTEGER,   // order status
                            Types.INTEGER,   // remaining quantity,
                            Types.VARCHAR,   // Match JSON string
                            Types.INTEGER    // Order id
                    });
        }

    }

}































