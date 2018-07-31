package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.Security;

import java.util.List;

public interface SecurityInterface {

    List<Security> getAllSecurities(String date, String time);

    String getTopSecuritiesByPrice(String date, String time);

    String getTopSecuritiesByCount(String date, String time);
}
