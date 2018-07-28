package com.cashEquityProject.cashEquity.model;

public class SecurityModel {

    private String date;
    private String time;
    private String symbol;
    private Double lastTradedPrice;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(Double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    @Override
    public String toString() {
        return "SecurityModel{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", symbol='" + symbol + '\'' +
                ", lastTradedPrice=" + lastTradedPrice +
                '}';
    }
}
