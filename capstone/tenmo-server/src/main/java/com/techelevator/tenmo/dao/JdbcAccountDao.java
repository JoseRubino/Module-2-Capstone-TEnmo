package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getCurrentBalance(Long userId) {
        String sql = "select balance from account " +
                     "join tenmo_user using(user_id) where user_id = ?;";
        Long balance = jdbcTemplate.queryForObject(sql, Long.class, userId);

        BigDecimal bigBalance = new BigDecimal(balance);
        return bigBalance;
    }

    @Override
    public List<String> getTransactionHistory() {
        return null;
    }

    @Override
    public void sendBucksToAccount() {

    }
}
