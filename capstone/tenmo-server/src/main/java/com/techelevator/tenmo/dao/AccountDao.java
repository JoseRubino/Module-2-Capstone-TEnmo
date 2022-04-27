package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getCurrentBalance();

    List<String> getTransactionHistory();

    void sendBucksToAccount();
}
