package ru.coffee.repository.impl;

import liquibase.exception.LiquibaseException;
import ru.coffee.config.DBConnectionProvider;
import ru.coffee.domain.User;
import ru.coffee.exception.NotEnoughMoneyException;
import ru.coffee.out.OutputStream;
import ru.coffee.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UserRepositoryBD implements UserRepository<User> {
    private DBConnectionProvider provider;
    private OutputStream<String> output;

    public UserRepositoryBD(OutputStream<String> output, DBConnectionProvider provider) {
        this.output = output;
        this.provider = provider;
    }

    @Override
    public User addUser(User user) {
        String sqlInsertInToOperationId =
                "INSERT INTO wallet.operation_detail (operation_id, operation_time, action)" +
                "VALUES (nextval('wallet.seq_operation_detail_id'), ?, ?)";
        try (Connection connection = provider.getConnection()) {
            connection.setAutoCommit(false);
            String registration = "Registration";
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            PreparedStatement statement = connection.prepareStatement(sqlInsertInToOperationId);
            statement.setString(1, currentTime);
            statement.setString(2, registration);
            statement.executeUpdate();
            String sql =
                    "INSERT INTO wallet.user (user_id, username, password, amount) " +
                    "VALUES (nextval('wallet.seq_user_id'), ?, ?, 10000.00)";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User addMoney(User user, BigDecimal money) {
        try (Connection connection = provider.getConnection()) {
            connection.setAutoCommit(false);
            saveOperationDetail(user, money, connection);
            String sql = "UPDATE wallet.user AS u " +
                         "SET amount = amount + ? " +
                         "WHERE u.username = ?";
            addOrWithdrawMoney(user, money, sql, connection);
            connection.commit();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User withdraw(User user, BigDecimal money) {
        try (Connection connection = provider.getConnection()) {
            if (user.getBalance().doubleValue() < Math.abs(money.doubleValue())) {
                throw new NotEnoughMoneyException("Not enough money at your amount");
            }
            connection.setAutoCommit(false);
            saveOperationDetail(user, money, connection);
            String sql = "UPDATE wallet.user AS u " +
                         "SET amount = amount - abs(?) " +
                         "WHERE u.username = ?";
            addOrWithdrawMoney(user, money, sql, connection);
            connection.commit();
        } catch (SQLException | NotEnoughMoneyException | LiquibaseException e) {
            output.output(e.getMessage());
        }
        return user;

    }

    @Override
    public Optional<User> findUser(User user) {
        String sql = "SELECT u.username, u.password, u.amount " +
                     "FROM wallet.user u " +
                     "WHERE u.username = ? " +
                     "AND u.password = ?";
        try (Connection connection = provider.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            User userFromTable = null;
            if (resultSet.next()) {
                userFromTable = new User();
                userFromTable.setName(resultSet.getString("username"));
                userFromTable.setPassword(resultSet.getString("password"));
                userFromTable.setBalance(new BigDecimal(resultSet.getString("amount")));
                connection.commit();
                return Optional.of(userFromTable);
            }
            return Optional.empty();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void history(User user) {
        String sqlHistory = "SELECT wu.username, wo.action " +
                            "FROM wallet.user wu " +
                            "JOIN wallet.operation_detail wo " +
                            "ON wu.user_id = wo.user_id " +
                            "WHERE wu.username = ?";
        try (Connection connection = provider.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlHistory);
            preparedStatement.setString(1, user.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                output.output(resultSet.getString("username") + ": " +
                              resultSet.getString("action" + "\n"));
            }
            connection.commit();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveOperationDetail(User user, BigDecimal money, Connection connection) throws SQLException {
        String sqlFindUser = "SELECT u.user_id " +
                             "FROM wallet.user u " +
                             "WHERE u.username = ?";
        PreparedStatement findOperationId = connection.prepareStatement(sqlFindUser);
        findOperationId.setString(1, user.getName());
        ResultSet resultSet = findOperationId.executeQuery();

        int userId = 0;
        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
        }

        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String sqlSaveOperation = "INSERT INTO wallet.operation_detail (operation_id, operation_time, action, user_id) " +
                                  "VALUES (nextval('wallet.seq_operation_detail_id'), ?, ?, ?)";
        PreparedStatement saveOperation = connection.prepareStatement(sqlSaveOperation);
        saveOperation.setString(1, currentTime);
        saveOperation.setBigDecimal(2, money);
        saveOperation.setInt(3, userId);
        saveOperation.executeUpdate();
    }

    private void addOrWithdrawMoney(User user, BigDecimal money, String sql, Connection connection) throws SQLException {
        String selectAmount = "SELECT u.amount " +
                              "FROM wallet.user u " +
                              "WHERE u.username = ?";
        ResultSet resultSet;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, money);
        preparedStatement.setString(2, user.getName());
        preparedStatement.executeUpdate();
        PreparedStatement updatedUser = connection.prepareStatement(selectAmount);
        updatedUser.setString(1, user.getName());
        resultSet = updatedUser.executeQuery();
        if (resultSet.next()) {
            user.setBalance(new BigDecimal(resultSet.getString("amount")));
        }
    }
}
