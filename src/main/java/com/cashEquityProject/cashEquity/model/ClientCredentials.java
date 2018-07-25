package com.cashEquityProject.cashEquity.model;

public class ClientCredentials {

//    private Integer id;
    private String clientCode, password;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientCode() {
        return clientCode;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Client code : " + clientCode + "\n" + "Password: " + password;
    }
}
