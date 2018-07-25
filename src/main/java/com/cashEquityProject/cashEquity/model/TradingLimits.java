package com.cashEquityProject.cashEquity.model;

public class TradingLimits {

    private String nameOfEmployee;
    private String desk;
    private String designation;
    private Double sectorLimit;
    private Double overallLimit;
    private String sector1;
    private String sector2;

    public String getNameOfEmployee() {
        return nameOfEmployee;
    }

    public void setNameOfEmployee(String nameOfEmployee) {
        this.nameOfEmployee = nameOfEmployee;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getSectorLimit() {
        return sectorLimit;
    }

    public void setSectorLimit(Double sectorLimit) {
        this.sectorLimit = sectorLimit;
    }

    public Double getOverallLimit() {
        return overallLimit;
    }

    public void setOverallLimit(Double overallLimit) {
        this.overallLimit = overallLimit;
    }

    public String getSector1() {
        return sector1;
    }

    public void setSector1(String sector1) {
        this.sector1 = sector1;
    }

    public String getSector2() {
        return sector2;
    }

    public void setSector2(String sector2) {
        this.sector2 = sector2;
    }

    @Override
    public String toString() {
        return "TradingLimits{" +
                "nameOfEmployee='" + nameOfEmployee + '\'' +
                ", desk='" + desk + '\'' +
                ", designation='" + designation + '\'' +
                ", sectorLimit=" + sectorLimit +
                ", overallLimit=" + overallLimit +
                ", sector1='" + sector1 + '\'' +
                ", sector2='" + sector2 + '\'' +
                '}';
    }
}
