package com.youtube.javaspringsimplepicpay.services;

import com.youtube.javaspringsimplepicpay.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {
    private static final String AUTHORIZATION_URL = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";
    private static final String AUTHORIZED_MESSAGE = "Autorizado";

    @Autowired
    private RestTemplate restTemplate;

    public Boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(AUTHORIZATION_URL, Map.class);

        boolean isStatusOK = HttpStatus.OK.equals(authorizationResponse.getStatusCode());
        boolean isMessageAuthorized = AUTHORIZED_MESSAGE.equalsIgnoreCase((String) authorizationResponse.getBody().get("message"));

        return isStatusOK && isMessageAuthorized;
    }
}
