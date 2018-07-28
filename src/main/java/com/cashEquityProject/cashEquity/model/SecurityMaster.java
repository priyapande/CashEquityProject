package com.cashEquityProject.cashEquity.model;

public class SecurityMaster {
    private String companyName;
    private String sector;
    private String symbol;
    private String isin;
    private Integer marketLot;
    private Integer priceVarianceLimit;
    private Double price;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Integer getMarketLot() {
        return marketLot;
    }

    public void setMarketLot(Integer marketLot) {
        this.marketLot = marketLot;
    }

    public Integer getPriceVarianceLimit() {
        return priceVarianceLimit;
    }

    public void setPriceVarianceLimit(Integer priceVarianceLimit) {
        this.priceVarianceLimit = priceVarianceLimit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SecurityMaster{" +
                "companyName='" + companyName + '\'' +
                ", sector='" + sector + '\'' +
                ", symbol='" + symbol + '\'' +
                ", isin='" + isin + '\'' +
                ", marketLot=" + marketLot +
                ", priceVarianceLimit=" + priceVarianceLimit +
                ", price=" + price +
                '}';
    }
}
