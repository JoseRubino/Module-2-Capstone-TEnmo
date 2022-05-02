package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "transfers/")
public class TransferController {
    AccountDao accountDao;
    TransferDao transferDao;


    @RequestMapping(value = "/new", method = RequestMethod.PUT)
    public void addTransfer(@RequestBody Transfer transfer) throws InsufficientFundsException {
        transferDao.createTransfer(transfer);
        accountDao.withdrawFromAccount(transfer.getAccountFrom(), transfer.getAmount());
        accountDao.depositToAccount(transfer.getAccountTo(), transfer.getAmount());
    }

    @GetMapping("/user/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }
    @GetMapping("/transfers/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferByTransferId(id);
    }

    @RequestMapping(path="getAll", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }
}

