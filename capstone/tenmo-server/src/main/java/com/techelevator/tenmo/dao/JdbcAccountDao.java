package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.math.BigDecimal;


@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
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
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if(result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if(result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public void updateAccount(Account accountToUpdate) {}

    @Override
    public void depositToAccount(Account accountToUpdate, BigDecimal amount) {
        String sql = "UPDATE accounts " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance().add(amount), accountToUpdate.getAccountId());
    }
    @Override
    public void withdrawFromAccount(Account accountToUpdate, BigDecimal amount) {
        String sql = "UPDATE accounts " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance().subtract(amount), accountToUpdate.getAccountId());
    }

    private Account mapResultsToAccount(SqlRowSet result) {
        int accountId = result.getInt("account_id");
        int userAccountId = result.getInt("user_id");
        BigDecimal accountBalance = new BigDecimal(result.getInt("balance"));
//        Balance balance = new Balance();
//        String accountBalance = result.getString("balance");
//        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(accountId, userAccountId, accountBalance);
    }
    }







