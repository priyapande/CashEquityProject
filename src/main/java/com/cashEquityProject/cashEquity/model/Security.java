package com.cashEquityProject.cashEquity.model;

public class Security {

    private String companyName;         // Name of company
    private String sector;              // Security sector
    private String symbol;              // Symbol of security (primary key)
    private String ISIN;                // ISIN number of security (unique for security)
    private Integer marketLot;          // Minimum trading quantity (usually 1)
    private Integer priceVarianceLimit; // Price Variant Limit
    private Double price;               // Price of the security (time variant)
    private Integer buyCount;           // Tracking number of buy orders
    private Integer sellCount;          // Tracking number of sell orders

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

    public String getISIN() {
        return ISIN;
    }

    public void setISIN(String ISIN) {
        this.ISIN = ISIN;
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

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    @Override
    public String toString() {
        return "Security{" +
                "companyName='" + companyName + '\'' +
                ", sector='" + sector + '\'' +
                ", symbol='" + symbol + '\'' +
                ", ISIN='" + ISIN + '\'' +
                ", marketLot=" + marketLot +
                ", priceVarianceLimit=" + priceVarianceLimit +
                ", price=" + price +
                ", buyCount=" + buyCount +
                ", sellCount=" + sellCount +
                '}';
    }
}
