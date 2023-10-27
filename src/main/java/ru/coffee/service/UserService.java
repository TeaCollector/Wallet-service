package ru.coffee.service;

import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service with base action for repository.
 */

public interface UserService<T> {

    T addUser(User user);

    T addMoney(User user, BigDecimal money);

    T withdraw(User user, BigDecimal money);

    Optional<User> findUser(User user);

    void history(User user);
}
