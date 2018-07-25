package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.Authentication;
import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController implements config {

    @Autowired
    Authentication authentication;

    @RequestMapping(value="/login")
    public Integer login(@RequestBody ClientCredentials clientCredentials) {
        return authentication.authenticate(clientCredentials);
    }




}
