package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    User getUserByAccount(long id);

    User findByUserId(long id);

    List<Transfer> getTransfersByUser(long userId) throws TransferNotFoundException;

    BigDecimal getBalanceById(long id);

    int getAccountByUserId(long id);

    int findIdByUsername(String username);


    boolean create(String username, String password);



}
