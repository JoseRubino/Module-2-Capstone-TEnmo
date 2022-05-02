package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        baseUrl = url;
    }

    public void getTransfer(AuthenticatedUser user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(user.getToken());
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(user, httpHeaders);

        Transfer transfer = restTemplate.exchange(baseUrl + "transfers/get",
                HttpMethod.GET, entity, Transfer.class).getBody();
        System.out.println("Your transaction history: " + transfer);
    }

    public Transfer[] getAllTransfers(AuthenticatedUser user) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setBearerAuth(user.getToken());
//        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(user, httpHeaders);
//
//        ResponseEntity<List> transfers = restTemplate.exchange(baseUrl + "transfers/getAll",
//                HttpMethod.GET, entity, List.class);
//        System.out.println("Your transaction history: " + transfers.getBody());
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "transfers/getAll", HttpMethod.GET, makeEntity(user), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request");
        } catch (ResourceAccessException e)  {
            System.out.println("Could not complete request due to server issue");
        }
        return transfers;
    }

    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        String url = baseUrl + "transfers/new";

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private HttpEntity<AuthenticatedUser> makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<AuthenticatedUser>(authenticatedUser, httpHeaders);
    }
}

