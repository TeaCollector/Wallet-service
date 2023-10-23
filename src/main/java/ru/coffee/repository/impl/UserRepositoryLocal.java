package ru.coffee.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.OperationDetail;
import ru.coffee.domain.User;
import ru.coffee.exception.NotEnoughMoneyException;
import ru.coffee.out.OutputStream;
import ru.coffee.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * User repository implementation
 */
public class UserRepositoryLocal implements UserRepository {

    private final Logger logger = LogManager.getLogger(UserRepositoryLocal.class.getName());
    private final OutputStream<String> output;
    private final List<User> userList = new ArrayList<>();
    private final Map<User, List<OperationDetail>> operationDetailList = new HashMap<>();

    public UserRepositoryLocal(OutputStream<String> output) {
        this.output = output;
    }

    @Override
    public void addUser(User user) {
        userList.add(user);
        logger.info("{} was added at database", user.getName());
    }


    @Override
    public void withdraw(User user, BigDecimal money) {
        logger.info("{} trying to withdraw {}", user.getName(), money);
        Optional<User> userToWithdrawMoney = findUser(user);
        User userFromOptional = userToWithdrawMoney.get();
        BigDecimal withDrawMoney = money.negate();
        BigDecimal newBalance = userFromOptional.getBalance().subtract(withDrawMoney);
        if (newBalance.longValue() < 0) {
            try {
                throw new NotEnoughMoneyException("Sorry, you don't have enough money.");
            } catch (NotEnoughMoneyException e) {
                logger.error("Users trying to withdraw incorrect sum.", e.getCause());
                output.output("You don't have enough money.\nOperation cancelled.");
                return;
            }
        }
        userList.remove(userFromOptional);
        userFromOptional.setBalance(newBalance);
        userList.add(userFromOptional);
        addOperationDetails(user, money);
        logger.info("{} withdraw {}", user.getName(), money);
    }


    @Override
    public void addMoney(User user, BigDecimal money) {
        logger.info("{} trying to add {}", user.getName(), money);
        Optional<User> optionalUser = findUser(user);
        User userFromOptional = optionalUser.get();
        userList.remove(userFromOptional);
        BigDecimal newBalance = userFromOptional.getBalance().add(money);
        userFromOptional.setBalance(newBalance);
        userList.add(userFromOptional);
        addOperationDetails(user, money);
        logger.info("{} add {}", user.getName(), money);

    }

    @Override
    public Optional<User> findUser(User user) {
        logger.info("Trying to find {}.", user.getName());
        for (User userFromList : userList) {
            if (userFromList.getName().equals(user.getName()) &&
                userFromList.getPassword().equals(user.getPassword())) {
                logger.info("{} was found.", userFromList.getName());
                return Optional.of(userFromList);
            }
        }
        logger.warn("{} was not found", user.getName());
        return Optional.empty();
    }

    /**
     * Show all user's history
     * @param user on which we will show his history operation
     */
    @Override
    public void history(User user) {
        logger.info("{} request his history of operation.", user.getName());
        for (Map.Entry<User, List<OperationDetail>> detail : operationDetailList.entrySet()) {
            if (detail.getKey().getName().equals(user.getName()) &&
                detail.getKey().getPassword().equals(user.getPassword())) {
                output.output("User '" + detail.getKey().getName() + "': " + detail.getValue() + "\n");
            }
        }
    }

    @Override
    public String currentAmount(User user) {
        return null;
    }

    /**
     * For save operation detail at db
     * @param user which make action
     * @param action action withdraw or add money
     */
    private void addOperationDetails(User user, BigDecimal action) {
        OperationDetail operationDetail = new OperationDetail();
        operationDetail.setAction(action);
        operationDetail.setBeginOperationTIme(LocalTime.now().format(DateTimeFormatter.ofPattern("hh.mm.ss")));
        operationDetailList.put(user, List.of(operationDetail));
    }
}
