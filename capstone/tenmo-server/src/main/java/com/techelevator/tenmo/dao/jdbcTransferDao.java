package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcUserDao userDao;

    @Override
    public Transfer get(Long id) throws TransferNotFoundException {
        Transfer transfer = new Transfer();
        int accountFrom;
        int accountTo;
        User userFrom = new User();
        User userTo = new User();

        String sql = "select * from transfer\n" +
                     "join transfer_type using(transfer_type_id)\n" +
                     "join transfer_status using(transfer_status_id);";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        if (row.next()){
            accountFrom = row.getInt("account_from");
            accountTo = row.getInt("account_to");

            userFrom = userDao.getUserByAccount(accountFrom);
            userTo = userDao.getUserByAccount(accountTo);

            transfer = mapRowToTransfer(row, userFrom.getUsername(), userTo.getUsername());
            return transfer;
        }
        throw new TransferNotFoundException();
    }


    @Override
    public Transfer create(Transfer transfer) {
        return null;
    }

    @Override
    public Transfer update(Transfer transfer) {
        return null;
    }




    private Transfer mapRowToTransfer(SqlRowSet row, String userFrom, String userTo) {
        Transfer transfer = new Transfer();
        transfer.setId(row.getInt("transfer_id"));
        transfer.setUserFrom(userFrom);
        transfer.setUserTo(userTo);
        transfer.setTransferType(row.getString("type"));
        transfer.setTransferStatus(row.getString("status"));
        transfer.setAmount(row.getBigDecimal("amount"));

        return transfer;

    }}
