package com.cashEquityProject.cashEquity.controller;

import com.cashEquityProject.cashEquity.implementation.Authentication;
import com.cashEquityProject.cashEquity.implementation.ClientMasterImplementation;
import com.cashEquityProject.cashEquity.model.ClientCredentials;
import com.cashEquityProject.cashEquity.repository.config;
import org.jboss.logging.Param;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(allowedHeaders =
        {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class MyController{

    @Autowired
    Authentication authentication;

    @Autowired
    ClientMasterImplementation clientMasterImplementation;

    private Integer loginStatus;

    @PostMapping(value = "/login")
    public String login(@RequestBody ClientCredentials clientCredentials) {
        Integer response = authentication.authenticate(clientCredentials);

        loginStatus = response;

        JSONObject jsonObject = new JSONObject();

        String msg = "";
        if (response.equals(config.SUCCESS)) {
            msg = "Login succesfull";
        } else if (response.equals(config.INVALID_USER)) {
            msg = "Invalid User";
        } else if (response.equals(config.INVALID_PASSWORD)) {
            msg = "Invalid Password";
        } else if (response.equals(config.FAILED)) {
            msg = "Login Failed due to other problems";
        } else {
            System.out.println("Unreachable code.");
        }

        jsonObject.put("status", response);
        jsonObject.put("message", msg);

        if (response.equals(config.SUCCESS)) {

            JSONObject user = clientMasterImplementation.userInfo(clientCredentials.getClientCode());

            jsonObject.put("code", user.get("code"));
            jsonObject.put("name", user.get("name"));

        }

        return jsonObject.toString();


    }

    @PostMapping(value = "/ordersByClientCode/{code}")
    public String getOrdersByClientCode(@PathVariable String code) {

        return "";

    }

}
