package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/transfers")
public class TransferController {
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;

    @GetMapping()
    public Transfer getTransfer(Principal principal) throws TransferNotFoundException {
        User user = userDao.findByUsername(principal.getName());
        return transferDao.get(user.getId());
    }
}
