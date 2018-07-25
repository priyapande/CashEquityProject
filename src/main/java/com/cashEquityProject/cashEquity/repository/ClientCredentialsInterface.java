package com.cashEquityProject.cashEquity.repository;

import com.cashEquityProject.cashEquity.model.ClientCredentials;

public interface ClientCredentialsInterface {

    Integer authenticate(ClientCredentials clientCredentials);

}
