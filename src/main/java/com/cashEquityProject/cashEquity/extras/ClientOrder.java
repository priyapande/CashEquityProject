package com.cashEquityProject.cashEquity.extras;

import ch.qos.logback.core.net.server.Client;

public class ClientOrder {

    private String clientCode;
    private Integer order;

    public ClientOrder() {}

    public ClientOrder(String clientCode, Integer order) {
        this.clientCode = clientCode;
        this.order = order;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
