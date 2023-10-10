package ru.coffee.repository;

import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Basic interface for action with user
 */
public interface UserRepository {

    /**
     * @param user for adding at db.
     */
    void addUser(User user);

    /**
     * @param user on which we will withdraw money
     * @param id uniq transaction id
     * @param money amount to withdraw
     * @throws UserNotFoundException
     */
    void withdraw(User user, String id, BigDecimal money) throws UserNotFoundException;

    /**
     * @param user on which we will add money
     * @param id uniq transaction id
     * @param money amount to add
     * @throws UserNotFoundException
     */
    void addMoney(User user, String id, BigDecimal money) throws UserNotFoundException;

    /**
     * @param user for find in db
     * @return if empty, user not found, otherwise user itself
     */
    Optional<User> findUser(User user);

    /**
     * @param id uniq transaction id for save in db
     */
    void saveTransactionId(String id);

    /**
     * Method to show history of operation
     * @param user on which we will show his history operation
     */
    void history(User user);
}
