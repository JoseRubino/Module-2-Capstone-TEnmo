package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/users")
public class UserController {
@Autowired
    UserDao userDao;
@Autowired
    AccountDao accountDao;


    @RequestMapping()
    public List<User> getAll()
    {
        List<User> users = userDao.findAll();
        return users;
    }


    @GetMapping("/balance")
    public BigDecimal getBalance (Principal principal){
    User user = userDao.findByUsername(principal.getName());
        return accountDao.getCurrentBalance(user.getId());
    }

    @GetMapping("/balance/{id}")
    public BigDecimal getBalance(@PathVariable int id){
    User user = userDao.getUserByUserId(id);
    return accountDao.getCurrentBalance(user.getId());
    }
    @GetMapping(path="/account/{user_id}")
    public Account getAccountByUserId(@PathVariable int user_id) {
        Account account = accountDao.getAccountByUserID(user_id);
        System.out.println(account);
        return account;
    }

    @GetMapping(path="/{id}")
    public User getUserByUserId(@PathVariable int id) {
        return userDao.getUserByUserId(id);
    }
}
