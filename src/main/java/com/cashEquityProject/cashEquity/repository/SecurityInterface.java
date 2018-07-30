package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Security;

import java.util.List;

public interface SecurityInterface {

    List<Security> getAllSecurities(String date, String time);

    List<Security> getTopSecuritiesByPrice();

    List<Security> getTopSecuritiesByCount();
}
