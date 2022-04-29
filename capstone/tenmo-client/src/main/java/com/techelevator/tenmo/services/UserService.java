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


    public User[] getAllUsers(AuthenticatedUser authenticatedUser) {

        User[] users = null;
        try {
          users = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, makeEntity(authenticatedUser), User[].class).getBody();

        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request");
        }catch (ResourceAccessException e)  {
            System.out.println("Could not complete request due to server issue");
        }
        return users;
    }
    public AuthenticatedUser getUserByUserId(AuthenticatedUser user, int userId) {

        HttpEntity<AuthenticatedUser> entity = makeEntity(user);
        return restTemplate.exchange(baseUrl + "users/" + userId, HttpMethod.GET, entity, AuthenticatedUser.class).getBody();
    }

    private HttpEntity<AuthenticatedUser> makeEntity(AuthenticatedUser authenticatedUser) {
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setBearerAuth(authenticatedUser.getToken());
       return new HttpEntity<AuthenticatedUser>(authenticatedUser, httpHeaders);
    }
}

