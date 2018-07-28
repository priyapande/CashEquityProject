package com.cashEquityProject.cashEquity.model;

public class SecurityModel {

    private String date;
    private Integer hours;
    private Integer minutes;
    private String symbol;
    private String isinNo;
    private Double lastTradedPrice;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIsinNo() {
        return isinNo;
    }

    public void setIsinNo(String isinNo) {
        this.isinNo = isinNo;
    }

    public Double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(Double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    @Override
    public String toString() {
        return "SecurityModel {" +
                "  date='" + date + '\'' +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", symbol='" + symbol + '\'' +
                ", isinNo='" + isinNo + '\'' +
                ", lastTradedPrice=" + lastTradedPrice +
                "}";
    }
}
