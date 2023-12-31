package ru.coffee.repository;

import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Basic interface for action with user
 */
public interface UserRepository<T> {

    /**
     * @param user for adding at db.
     */
    User addUser(User user);

    /**
     * @param user on which we will add money
     * @param money amount to add
     * @throws UserNotFoundException
     */
    T addMoney(User user, BigDecimal money);

    /**
     * @param user on which we will withdraw money
     * @param money amount to withdraw
     * @throws UserNotFoundException
     */
    T withdraw(User user, BigDecimal money);

    /**
     * @param user for find in db
     * @return if empty, user not found, otherwise user itself
     */
    Optional<User> findUser(User user);

    /**
     * Method to show history of operation
     * @param user on which we will show his history operation
     */
    void history(User user);

    /**
     * return current money of user
     */

}
