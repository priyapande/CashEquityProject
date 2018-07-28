package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.SecurityImplementation;
import com.cashEquityProject.cashEquity.model.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class SecurityController {

    @Autowired
    SecurityImplementation securityImplementation;

    @RequestMapping(value = "/getAllSecurities/{date}/{time}")
    public List<Security> getAllSecurities(String date, String time) {
        return securityImplementation.getAllSecurities(date, time);
    }

}
