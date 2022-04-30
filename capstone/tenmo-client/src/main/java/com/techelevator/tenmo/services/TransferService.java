package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user = null;

    public TransferService(String url) {
        baseUrl = url;
    }

    public void setUser(AuthenticatedUser user)
    {
        this.user = user;
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

    public AuthenticatedUser createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "transfers/new";

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class).getBody();
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("You're broke, bud")) {
                System.out.println("You don't have enough money for that transaction.");
            } else {
                System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
            }
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return authenticatedUser;
    }


}

