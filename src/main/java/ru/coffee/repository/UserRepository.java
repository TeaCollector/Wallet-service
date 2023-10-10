package ru.coffee.repository;

import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository {
    void addUser(User user);

    void withdraw(User user, String token, BigDecimal money) throws UserNotFoundException;

    void addMoney(User user, String token, BigDecimal money) throws UserNotFoundException;

    Optional<User> findUser(User user);

    void saveTransactionId(String id);

    void history(User user);
}
