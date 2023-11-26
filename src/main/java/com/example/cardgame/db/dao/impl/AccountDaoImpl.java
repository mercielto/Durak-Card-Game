package com.example.cardgame.db.dao.impl;

import com.example.cardgame.db.dao.AbstractDao;
import com.example.cardgame.db.dao.daoExtend.AccountDao;
import com.example.cardgame.db.model.Account;

import java.sql.*;
import java.util.List;

public class AccountDaoImpl extends AbstractDao<Account> implements AccountDao {
    private static final String SQL_GET_ALL = "SELECT * FROM account";
    private static final String SQL_GET_BY_ID = "SELECT * FROM account WHERE id = ?";
    private static final String SQL_INSERT = "INSERT";

    public AccountDaoImpl(Connection c) {
        super(c);
    }

    @Override
    public List<Account> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL);
            return executeSqlPreparedStatement(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Account getById(Long id) {
        return null;
    }

    @Override
    public void update(Account entity) {

    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean insert(Account entity) {
        return false;
    }
}
