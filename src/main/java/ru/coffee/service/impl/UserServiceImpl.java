package ru.coffee.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.repository.UserRepository;
import ru.coffee.service.UserService;

import java.math.BigDecimal;
import java.util.Optional;

public class UserServiceImpl implements UserService<User> {

    private UserRepository<User> userRepository;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());

    public UserServiceImpl(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        logger.info("Adding {}", user.getName());
        return userRepository.addUser(user);
    }

    @Override
    public User addMoney(User user, BigDecimal money) {
        logger.info("Adding money for {} amount {}", user.getName(), money);
        return userRepository.addMoney(user, money);

    }

    @Override
    public User withdraw(User user, BigDecimal money) {
        logger.info("Withdraw money for {} amount {}", user.getName(), money);
        return userRepository.withdraw(user, money);
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
