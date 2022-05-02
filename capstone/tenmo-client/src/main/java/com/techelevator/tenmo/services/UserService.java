package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
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
        } catch (ResourceAccessException e)  {
            System.out.println("Could not complete request due to server issue");
        }
        return users;
    }

    public User getUserByUserId(AuthenticatedUser user, int userId) {
        ResponseEntity<User> response = restTemplate.exchange(baseUrl + "users/" + userId, HttpMethod.GET, makeEntity(user), User.class);
        return response.getBody();
    }

    private HttpEntity<AuthenticatedUser> makeEntity(AuthenticatedUser authenticatedUser) {
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setBearerAuth(authenticatedUser.getToken());
       return new HttpEntity<AuthenticatedUser>(authenticatedUser, httpHeaders);
    }
}

