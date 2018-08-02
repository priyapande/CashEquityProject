package com.cashEquityProject.cashEquity.implementation;

import com.cashEquityProject.cashEquity.extras.*;
import com.cashEquityProject.cashEquity.model.Order;
import com.cashEquityProject.cashEquity.model.TopSecuritiesCount;
import com.cashEquityProject.cashEquity.repository.OrdersInterface;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONObject;
import com.cashEquityProject.cashEquity.repository.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.*;
import java.util.logging.Logger;


@Repository
public class OrdersImplementation implements OrdersInterface {

    /*
     * Repository class handling OrdersInterface.
     * Implements functions to add, get and delete orders.
     */

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private HashMap<ClientOrder, Boolean> reviewedOrders;

    private static final Logger logger = Logger.getLogger(OrdersImplementation.class.getName());

    @Override
    public void addOrder(Order order){
        /*
         * Add client order to MYSQL table using Order object.
         * Args:
         *  order : Order object.
         */

        String sqlUpdateCount;

        // Default order status is 0 i.e. un-executed order
        order.setOrderStatus(0);
        order.setRemainingquantity(order.getQuantity());
        order.setValue(order.getLimitPrice() * order.getQuantity());
        order.setMatches("");

        // Insert command (no orderId because it is auto incremented by MySQL)
        String sql = "insert into orders" +
                    " (clientcode, symbol, tradedate, tradetime, quantity, tradetype, limitprice, direction, value," +
                    " orderStatus, remainingquantity, matches)" +
                    " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                new Object[]{
                        order.getClientCode(),
                        order.getSymbol(),
                        order.getTradedate(),
                        order.getTradetime(),
                        order.getQuantity(),
                        order.getTradeType(),
                        order.getLimitPrice(),
                        order.getDirection(),
                        order.getValue(),
                        order.getOrderStatus(),
                        order.getRemainingquantity(),
                        order.getMatches()
                },
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
                        Types.INTEGER,   // remaining quantity
                        Types.VARCHAR    // Matches JSON
        });

        if(order.getDirection().equals('B')) {
            sqlUpdateCount = "update securities set buycount = buycount + 1 where symbol = ?";
        } else if (order.getDirection().equals('S')) {
            sqlUpdateCount = "update securities set sellcount = sellcount + 1 where symbol = ?";
        } else {
            sqlUpdateCount = "";
            logger.severe("Order direction was neither B nor S.");
        }

        jdbcTemplate.update(sqlUpdateCount,
                            new Object[]{order.getSymbol()},
                            new int[]{Types.VARCHAR});

        Integer orderid = (Integer) jdbcTemplate.queryForObject("select MAX(orderid) from orders",
                                        new Object[]{},
                                        Integer.class);

        order.setOrderId(orderid);

        Nettingv2 netting = new Nettingv2(order, jdbcTemplate);
        new Thread(netting).start();

    }

    @Override
    public List<Order> getOrders(String code){
        /*
         * Get client orders from MYSQL table using client code.
         * Args:
         *  code : Client code.
         */

        String sql = "select * from orders where clientCode = ?";

        System.out.println("Before");

        List<Order> orders = jdbcTemplate.query(sql,
                new Object[]{code},
                new BeanPropertyRowMapper<>(Order.class));

        System.out.println("After");
        return orders;

//        Double balance;
//        JSONObject jsonObject;
//        JSONArray jsonArray = new JSONArray();
//
//        for (Order order: orders) {
//            balance = order.getLimitPrice() * (order.getQuantity() - order.getRemainingquantity());
//            if (order.getDirection().equals('B')) {
//                balance = -1 * balance;
//            }
//
//            jsonObject = order.toJSON();
////            jsonObject.put("balance", balance);
//            jsonArray.put(jsonObject);
//
//        }
//
//        return jsonArray.toString(4);

    }

    @Override
    public void deleteOrder(String orderId){
        /*
         * Delete client order from MYSQL table using order id.
         * Args:
         *  orderId : Order Id.
         */

        String sql = "update orders set orderstatus = " + config.EXECUTED  + " where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }

    @Override
    public void cancelOrder(String orderId){
        /*
         * For orders that have been cancelled by the client
         * Args:
         *  orderId : Order Id.
         */

        String sql = "update orders set orderstatus = " + config.ORDER_CANCELLED  + " where orderId=?";

        jdbcTemplate.update(sql,
                new Object[]{orderId},
                new int[]{Types.VARCHAR});
    }

    @Override
    public String getTopOrders(String symbol){
        /*
         * Get top buy and sell orders from MYSQL table using security symbol.
         * Args:
         *  code : security symbol.
         */


        // TODO: Date and time for selection of orders.
        // TODO: Consider pricevariancelimit


        // Top 5 Buy Orders
        String sql1 = "select * from orders where symbol = ? and direction = 'B' and orderstatus in (0, 1) order by limitprice DESC limit 5";

        List<Order> securityList = jdbcTemplate.query(sql1,
                                         new Object[]{symbol},
                                         new BeanPropertyRowMapper<>(Order.class));

        JSONArray buyArray = new JSONArray();
        for (Order order: securityList) {
            JSONObject buyObject = new JSONObject();
            buyObject.put("price", order.getLimitPrice());
            buyArray.put(buyObject);
        }

        // Top 5 Sell Orders
        String sql2 = "select * from orders where symbol = ? and direction = 'S' and orderstatus in (0, 1) order by limitprice ASC limit 5";

        List<Order> securityList1 = jdbcTemplate.query(sql2,
                            new Object[]{symbol},
                            new BeanPropertyRowMapper<>(Order.class));

        JSONArray sellArray = new JSONArray();
        for (Order order: securityList1) {
            JSONObject sellObject = new JSONObject();
            sellObject.put("price", order.getLimitPrice());
            sellArray.put(sellObject);
        }

        JSONArray result = new JSONArray();
        result.put(buyArray);
        result.put(sellArray);

        return result.toString();
    }

    @Override
    public String getClientReport(String clientCode) {


        JSONArray result = new JSONArray();
        reviewedOrders = new HashMap<ClientOrder, Boolean>();

        String sql = "select * from orders where clientCode = ?";
        List<Order> orders = jdbcTemplate.query(sql,
                                                new Object[] {clientCode},
                                                new BeanPropertyRowMapper<>(Order.class)
                                                );

        // Get unique symbols
        Set<String> symbols = new LinkedHashSet<String>();
        for (Order order: orders) {
            symbols.add(order.getSymbol());
        }

        for (String symbol: symbols) {

            Double balance = 0.0;
            Integer quantity = 0;
            // Integer remaining = 0;
            BalanceQuantity balanceQuantity;
            JSONObject jsonObject = new JSONObject();
            Iterator<Order> iterator = orders.iterator();

            while (iterator.hasNext()) {

                Order order = iterator.next();

                if (order.getSymbol().equals(symbol)) {

                    balanceQuantity = getOrderBalance(order);
                    quantity += balanceQuantity.getQuantity();

                    if (order.getDirection().equals('B')) {
                        balance += -1*balanceQuantity.getBalance();
                    } else {
                        balance += balanceQuantity.getBalance();
                    }

                    // remaining += order.getRemainingquantity();

                    iterator.remove();
                }
            }

            jsonObject.put("quantity", quantity);     // Total Quantity processed
            jsonObject.put("balance", balance);          // Net payable/receivable
            jsonObject.put("symbol", symbol);            // Security Symbol
            // jsonObject.put("remaining", remaining);      // Remaining quantity

            result.put(jsonObject);

        }

        return result.toString();

    }

    private BalanceQuantity getOrderBalance(Order order) {

        Double balance = 0.0;
        Integer quantity = 0;
        JSONArray jsonArray;
        BalanceQuantity balanceQuantity = new BalanceQuantity();

        if (order.getMatches().equals("")) {

            logger.warning("No match column in order");

            balanceQuantity.setQuantity(0);
            balanceQuantity.setBalance(0.0);
            return balanceQuantity;
        }

        jsonArray = new JSONArray(order.getMatches());

        for (Object match: jsonArray) {

            Integer orderid = ((JSONObject) match).getInt("orderid");
            String clientcode = ((JSONObject) match).getString("client");

            ClientOrder clientOrder = new ClientOrder(clientcode, orderid);
            Integer quan;

            quan = ((JSONObject) match).getInt("quantity");

            if (reviewedOrders.get(clientOrder) == null) {
                quantity += quan;
            }

            balance += (quan * ((JSONObject) match).getDouble("price"));

        }

        balanceQuantity.setBalance(balance);
        balanceQuantity.setQuantity(quantity);

        return balanceQuantity;

    }
}
