package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTransferDao transferDao;




    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public User getUserByAccount(long id) {
        User user = new User();

        String sql = "SELECT t.user_id "
                + "FROM tenmo_user AS t "
                + "JOIN accounts AS a "
                + " ON t.user_id = a.user_id "
                + "WHERE a.account_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        if(row.next())
        {
            int userId = row.getInt("user_id");
            user = findByUserId(userId);
            return user;
        }
        throw new AccountNotFoundException();

    }


    @Override
    public User findByUserId(long id) {
        String sql = "select * from tenmo_user where user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (result.next())
        {
            User user = mapRowToUser(result);
            return user;
        }
        throw new UsernameNotFoundException("User ID " + id + " was not found.");

    }

    @Override
    public List<Transfer> getTransfersByUser(long id) throws TransferNotFoundException {
        List<Transfer> transfers = new ArrayList<Transfer>();
        int accountId = getAccountByUserId(id);

        String sql = "SELECT t.transfer_id "
                + ",t.account_from "
                + ",t.account_to "
                + ",tt.transfer_type_desc AS type "
                + ",ts.transfer_status_desc AS status "
                + ",t.amount "
                + "FROM transfers AS t "
                + "JOIN transfer_types AS tt "
                + "	ON t.transfer_type_id = tt.transfer_type_id "
                + "JOIN transfer_statuses AS ts "
                + "ON t.transfer_status_id = ts.transfer_status_id "
                + "WHERE t.account_from = ? "
                + "OR t.account_to = ?; ";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while(row.next()) {
            Transfer transfer = transferDao.get(row.getInt("transfer_id"));
            transfers.add(transfer);
        }

        return transfers;
    }


    @Override
    public BigDecimal getBalanceById(long userId) {
        int accountId = getAccountByUserId(userId);
        BigDecimal balance = BigDecimal.valueOf(0);

        String sql = "SELECT balance "
                + "FROM accounts "
                + "WHERE account_id = ?;";
        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);

        if(row.next())
        {
            balance = row.getBigDecimal("balance");
        }

        return balance;
    }

    @Override
    public int getAccountByUserId(long id) {
        int accountId = -1;

        String sql = "SELECT a.account_id "
                + "FROM accounts AS a "
                + "JOIN tenmo_user AS t "
                + "ON a.user_id = t.user_id "
                + "WHERE t.user_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        if(row.next())
        {
            accountId = row.getInt("account_id");
        }

        return accountId;
    }


    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }



    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // create account
        sql = "INSERT INTO account (user_id, balance) values(?, ?)";
        try {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
