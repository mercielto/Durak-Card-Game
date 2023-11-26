package com.example.cardgame.db;

import com.example.cardgame.db.dao.daoExtend.AccountDao;

public class DataBaseManager {
    private AccountDao accountDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
