package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping()
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
    @GetMapping(path="/account/{id}")
    public Account getAccountByUserId(@PathVariable int id) {
        return accountDao.getAccountByUserID(id);
    }
    @GetMapping(path="/{id}")
    public User getUserByUserId(@PathVariable int id) {
        return userDao.getUserByUserId(id);
    }
}
