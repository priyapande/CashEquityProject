package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.SecurityImplementation;
import com.cashEquityProject.cashEquity.model.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class SecurityController {

    @Autowired
    SecurityImplementation securityImplementation;

    @RequestMapping(value = "/getSecurity/{time}")
    public Security getSecurity(String time) {
        return securityImplementation.getSecurity(time);
    }

}
