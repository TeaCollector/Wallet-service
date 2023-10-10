package ru.coffee.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.repository.UserRepository;
import ru.coffee.service.UserService;

import java.math.BigDecimal;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public void addMoney(User user, String token, BigDecimal money) throws UserNotFoundException {
        userRepository.addMoney(user, token, money);
    }

    @Override
    public void withdraw(User user, String token, BigDecimal money) throws UserNotFoundException {
        userRepository.withdraw(user, token, money);
    }

    @Override
    public Optional<User> findUser(User user) {
        return userRepository.findUser(user);
    }

    @Override
    public void addTransactionId(String id) {
        userRepository.saveTransactionId(id);
    }

    @Override
    public void history(User user) {
        userRepository.history(user);
    }
}
