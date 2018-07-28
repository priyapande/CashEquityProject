package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Security;

import java.util.List;

public interface SecurityInterface {

    // Returns all securities (with price according to login time)
    List<Security> getAllSecurities(String time);

}
