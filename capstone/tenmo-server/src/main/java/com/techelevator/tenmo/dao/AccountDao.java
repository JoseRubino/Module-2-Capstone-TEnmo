package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getCurrentBalance(Long userId);

     BigDecimal updateBalance(long userId, BigDecimal balance);



}
