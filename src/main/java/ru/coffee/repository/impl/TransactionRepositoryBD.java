package ru.coffee.repository.impl;

import liquibase.exception.LiquibaseException;
import ru.coffee.repository.TransactionRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class TransactionRepositoryBD implements TransactionRepository {


    private Connection connection;

    public TransactionRepositoryBD() {
        try {
            connection = getConnection();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addTransactionId(String transactionId) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO wallet.transaction(transaction_id, transaction) " +
                                                "values (nextval('seq_transaction_id'), ?)");
            preparedStatement.setString(1, transactionId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionId;
    }

    @Override
    public boolean isValidTransaction(String transactionId) {
        try {
            String sql = "SELECT w.transaction " +
                         "FROM wallet.transaction w " +
                         "WHERE w.transaction = ?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            preparedStatement.setString(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return true;
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws LiquibaseException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("src/main/resources/db/db.properties")) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String url = properties.getProperty("postgres.url");
        String username = properties.getProperty("postgres.username");
        String password = properties.getProperty("postgres.password");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
