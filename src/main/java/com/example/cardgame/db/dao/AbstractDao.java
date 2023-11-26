package com.example.cardgame.db.dao;

import com.example.cardgame.db.utill.RowMapper.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements Dao<T> {
    protected Connection connection;
    private RowMapper<T> rowMapper;

    public AbstractDao(Connection c) {
        connection = c;
    }

    protected List<T> executeSqlPreparedStatement(PreparedStatement preparedStatement) {
        List<T> entities = new ArrayList<>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entities.add(rowMapper.from(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
