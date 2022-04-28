package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public void setAuthToken(String token){
        authToken = token;
    }

    public AccountService(String url) {
        baseUrl = url;
    }

    public void getAccountBalance(AuthenticatedUser user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(user.getToken());
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(user, httpHeaders);

        BigDecimal balance = restTemplate.exchange(baseUrl + "users/balance?id=" + user.getUser().getId(),
                HttpMethod.GET, entity, BigDecimal.class).getBody();
        System.out.println("Your current balance is: " + balance);
    }
}
