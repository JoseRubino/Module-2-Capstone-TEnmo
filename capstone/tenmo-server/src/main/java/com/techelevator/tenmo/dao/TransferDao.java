package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public Transfer get(int id) throws TransferNotFoundException;
    public Transfer create(Transfer transfer) throws TransferNotFoundException;
    public Transfer update(Transfer transfer) throws TransferNotFoundException;
}
