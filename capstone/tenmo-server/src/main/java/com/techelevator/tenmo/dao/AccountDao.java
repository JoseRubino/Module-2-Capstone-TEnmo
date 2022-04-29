package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getCurrentBalance(Long userId);

    Account getAccountByUserID(int userId);
    Account getAccountByAccountID(int accountId);

    void updateAccount(Account accountToUpdate);

    void depositToAccount(Account accountToUpdate, BigDecimal amount);

    void withdrawFromAccount(Account accountToUpdate, BigDecimal amount);
}




