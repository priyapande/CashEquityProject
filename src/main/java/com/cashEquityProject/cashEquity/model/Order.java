package com.cashEquityProject.cashEquity.model;

public class Order {

    private String orderId;
    private String clientName;
    private String security;
    private String date;
    private String time;
    private Integer quantity;
    private String tradeType;
    private Double limitPrice;
    private char direction;
    private Double value;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", security='" + security + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", quantity=" + quantity +
                ", tradeType='" + tradeType + '\'' +
                ", limitPrice=" + limitPrice +
                ", direction=" + direction +
                ", value=" + value +
                '}';
    }
}
