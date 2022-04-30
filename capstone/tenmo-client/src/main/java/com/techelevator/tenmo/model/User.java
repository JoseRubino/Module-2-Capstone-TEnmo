package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class User {

    private Long id;
    private String username;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public BigDecimal getBalance(){
        return balance;
    }
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId().equals(id)
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }

}
