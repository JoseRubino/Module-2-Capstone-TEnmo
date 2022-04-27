package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

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

}
