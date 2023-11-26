package com.example.cardgame.db.utill.RowMapper.impl;

import com.example.cardgame.db.utill.RowMapper.RowMapper;
import com.example.cardgame.db.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public Account from(ResultSet rs) throws SQLException {
        return null;
    }
}
