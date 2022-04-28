package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
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
    //	Method allows user to send or request bucks.
    public Transfer createTransfer(Transfer transfer)
    {
        Transfer newTransfer = null;
        if(this.user !=null)
        {
            try
            {
                newTransfer = restTemplate.postForObject(baseUrl, makeTransferEntity(transfer)
                        , Transfer.class);
            }
            catch (RestClientResponseException e)
            {

            }
        }
        return newTransfer;
    }
    //	Method lists all transfers for the current user.
    public List<Transfer> getAllTransfers()
    {
        List<Transfer> transfers = null;
        if(this.user !=null)
        {
            try
            {
                Transfer[] transferArray = restTemplate.exchange(baseUrl, HttpMethod.GET,
                        makeAuthEntity(), Transfer[].class).getBody();
                transfers = Arrays.asList(transferArray);
            }
            catch (RestClientResponseException e)
            {

            }
        }
        return transfers;
    }
    private HttpEntity makeAuthEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.user.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }}
