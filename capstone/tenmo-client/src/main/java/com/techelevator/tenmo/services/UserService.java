package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String apiBaseUrl) {
        baseUrl = apiBaseUrl;
    }


    public List<User> getAllUsers(AuthenticatedUser authenticatedUser) {

        List<User> users = null;
        try {
          users = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, makeEntity(authenticatedUser), List.class).getBody();

        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request");
        }catch (ResourceAccessException e)  {
            System.out.println("Could not complete request due to server issue");
        }
        return users;
    }

 private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(authenticatedUser.getToken());
    HttpEntity entity = new HttpEntity(httpHeaders);
    return entity;
}}

