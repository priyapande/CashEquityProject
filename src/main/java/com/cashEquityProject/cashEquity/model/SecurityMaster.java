package com.cashEquityProject.cashEquity.model;

public class SecurityMaster {
    public String company_name;
    public String sector;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Integer getMarket_lot() {
        return market_lot;
    }

    public void setMarket_lot(Integer market_lot) {
        this.market_lot = market_lot;
    }

    public Integer getPrice_variance_limit() {
        return price_variance_limit;
    }

    public void setPrice_variance_limit(Integer price_variance_limit) {
        this.price_variance_limit = price_variance_limit;
    }

    public String symbol;
    public String isin;
    public Integer market_lot;
    public Integer price_variance_limit;

}
