package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.SecurityImplementation;
import com.cashEquityProject.cashEquity.model.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Security> getAllSecurities(@PathVariable String date, @PathVariable String time) {
        return securityImplementation.getAllSecurities(date, time);
    }

    @RequestMapping(value = "/getTopSecuritiesByPrice/{date}/{time}")
    public String getTopSecuritiesByPrice(@PathVariable String date, @PathVariable String time) {
        return securityImplementation.getTopSecuritiesByPrice(date, time);
    }

    @RequestMapping(value = "/getTopSecuritiesByCount")
    public String getTopSecuritiesByCount() {
        return securityImplementation.getTopSecuritiesByCount();
    }

    @RequestMapping(value = "/getSecuritiesByClient/{date}/{time}/{clientCode}")
    public String getSecuritiesByClient(@PathVariable String date, @PathVariable String time, @PathVariable String clientCode) {
        return securityImplementation.getSecuritiesByClient(date, time, clientCode);
    }
}
