package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();


    public TransferService(String url) {
        baseUrl = url;
    }

    public void getTransfer(AuthenticatedUser user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(user.getToken());
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(user, httpHeaders);

        Transfer transfer = restTemplate.exchange(baseUrl + "users/transfers",
                HttpMethod.GET, entity, Transfer.class).getBody();
        System.out.println("Your transaction history: " + transfer);
    }
}
