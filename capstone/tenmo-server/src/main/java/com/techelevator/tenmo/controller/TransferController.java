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
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;

    @GetMapping()
    public List<Transfer> getCurrentUserTransfers(Principal principal) throws TransferNotFoundException {
        int id = userDao.findIdByUsername(principal.getName());
        List<Transfer> transfers = userDao.getTransfersByUser(id);

        return transfers;
    }
}
