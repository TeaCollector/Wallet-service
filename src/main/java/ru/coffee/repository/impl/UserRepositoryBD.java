package ru.coffee.repository.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.repository.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

public class UserRepositoryBD implements UserRepository {

    private Connection connection;

    public UserRepositoryBD() {
        try {
            getConnectionAndLiquibase();
            connection = getConnection();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUser(User user) {
        try {
            String registration = "Registration";
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String sqlInsertInToOperationId =
                    "INSERT INTO wallet.operation_detail (operation_id, operation_time, action)" +
                    "VALUES (nextval('wallet.seq_operation_detail_id'), ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlInsertInToOperationId);
            statement.setString(1, currentTime);
            statement.setString(2, registration);
            statement.executeUpdate();
            String sql =
                    "INSERT INTO wallet_db.wallet.user (user_id, username, password, operation_id, amount) " +
                    "VALUES (nextval('wallet.seq_user_id'), ?, ?, currval('wallet.seq_operation_detail_id'), 10000.00)";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            System.out.println("USER WAS CREATED AT METHOD");
            System.out.println("\npassword: " + user.getPassword());
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void withdraw(User user, BigDecimal money) throws UserNotFoundException {

    }

    @Override
    public void addMoney(User user, BigDecimal money) throws UserNotFoundException {
        try {
            String sql = "UPDATE wallet.user " +
                         "SET amount = amount + ? " +
                         "WHERE wallet.user.username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, new BigDecimal(money));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUser(User user) {
        String sql = "SELECT u.username, u.password, u.amount " +
                     "FROM wallet.user u " +
                     "WHERE u.username = ? " +
                     "AND u.username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            User userFromTable = new User();
            if (resultSet.next()) {
                userFromTable.setName(resultSet.getString("username"));
                userFromTable.setPassword(resultSet.getString("password"));
                userFromTable.setBalance(new BigDecimal(resultSet.getString("amount")));
            }
            return Optional.of(userFromTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void history(User user) {
    }

    @Override
    public String currentAmount(User user) {
        try {
            String sql = "SELECT amount FROM wallet.user WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            ResultSet result = preparedStatement.executeQuery();
            return result.getString("amount");
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

    public Connection getConnectionAndLiquibase() throws LiquibaseException {
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
        Database database = null;
        try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.dropAll();
        liquibase.update();
        return connection;
    }

}
