package com.cashEquityProject.cashEquity.model;

public class Order{

    private String orderId;     // unique ID for the order
    private String clientCode;  // Client code
    private String symbol;    // Security Symbol of the order's security
    private String tradedate;        // Date of trade (format e.g. 21-JUL-2018)
    private String tradetime;        // Time of trade (format e.g. 09:30)
    private Integer quantity;   // Quantity of shares.
    private String tradeType;   // Type of trade (LIMIT or MARKET)
    private Double limitPrice;  // Limit price (for LIMIT order) TODO: rename to just price? Maybe "limitPrice" term is for LIMIT ORDERS.
    private Character direction;// Order direction : B(BUY) or S(SELL)
    private Double value;       // Value of order = price x quantity
    private Integer orderStatus;// Status of the order TODO: Decide values of status
    private Integer remainingquantity; //Quantity of shares after netting and processing

    public Integer getRemainingquantity() {
        return remainingquantity;
    }

    public void setRemainingquantity(Integer remainingquantity) {
        this.remainingquantity = remainingquantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTradedate() {
        return tradedate;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }

    public String getTradetime() {
        return tradetime;
    }

    public void setTradetime(String tradetime) {
        this.tradetime = tradetime;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(Double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public Character getDirection() {
        return direction;
    }

    public void setDirection(Character direction) {
        this.direction = direction;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", clientCode='" + clientCode + '\'' +
                ", symbol='" + symbol + '\'' +
                ", tradedate='" + tradedate + '\'' +
                ", tradetime='" + tradetime + '\'' +
                ", quantity=" + quantity +
                ", tradeType='" + tradeType + '\'' +
                ", limitPrice=" + limitPrice +
                ", direction=" + direction +
                ", value=" + value +
                ", orderStatus=" + orderStatus +
                ", remainingquantity=" + remainingquantity +
                '}';
    }
}
