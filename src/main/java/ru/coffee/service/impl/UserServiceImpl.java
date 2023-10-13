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
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        logger.info("Adding {}", user.getName());
        userRepository.addUser(user);
    }

    @Override
    public void addMoney(User user, BigDecimal money) throws UserNotFoundException {
        logger.info("Adding money for {} amount {}", user.getName(), money);
        userRepository.addMoney(user, money);
    }

    @Override
    public void withdraw(User user, BigDecimal money) throws UserNotFoundException {
        logger.info("Withdraw money for {} amount {}", user.getName(), money);
        userRepository.withdraw(user, money);
    }

    @Override
    public Optional<User> findUser(User user) {
        logger.info("Trying to find {}", user.getName());
        return userRepository.findUser(user);
    }

    @Override
    public void history(User user) {
        logger.info("Show history {}", user.getName());
        userRepository.history(user);
    }
}
