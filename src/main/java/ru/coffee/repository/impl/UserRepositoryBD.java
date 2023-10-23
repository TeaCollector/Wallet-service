package ru.coffee.repository.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.repository.UserRepository;
import ru.coffee.util.Utils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class UserRepositoryBD implements UserRepository {

    private Connection connection;
    private Utils utils;

    @Override
    public void addUser(User user) {
        try {
            connection = utils.getConnection();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO user (id, username, email, operation_id, account) " +
                                                "VALUES (nextval('wallet.seq_id'), ?, ?, ?, 10000.00)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void withdraw(User user, BigDecimal money) throws UserNotFoundException {

    }

    @Override
    public void addMoney(User user, BigDecimal money) throws UserNotFoundException {

    }

    @Override
    public Optional<User> findUser(User user) {
        return Optional.empty();
    }

    @Override
    public void history(User user) {
    }

    p
}
