package ru.coffee.service;

import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service with base action for repository.
 */

public interface UserService {

    void addUser(User user);

    void addMoney(User user, String token, BigDecimal money) throws UserNotFoundException;

    void withdraw(User user, String token, BigDecimal money) throws UserNotFoundException;

    Optional<User> findUser(User user);

    void addTransactionId(String id);

    void history(User user);
}
