package com.cashEquityProject.cashEquity.model;

public class ClientMaster {
    private String name;
    private String code;
    private String country;
    private double tradingLimitUSD, tradingLimitRS;

    public ClientMaster(String name, String code, String country, double tradingLimitUSD, double tradingLimitRS) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.tradingLimitUSD = tradingLimitUSD;
        this.tradingLimitRS = tradingLimitRS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTradingLimitUSD() {
        return tradingLimitUSD;
    }

    public void setTradingLimitUSD(double tradingLimitUSD) {
        this.tradingLimitUSD = tradingLimitUSD;
    }

    public double getTradingLimitRS() {
        return tradingLimitRS;
    }

    public void setTradingLimitRS(double tradingLimitRS) {
        this.tradingLimitRS = tradingLimitRS;
    }
}
