package ru.coffee.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.out.OutputStream;
import ru.coffee.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class Utils {
    private final UserService userService;
    private final OutputStream output;
    private static final Logger logger = LogManager.getLogger(Utils.class.getName());

    public Utils(UserService userService, OutputStream output) {
        this.userService = userService;
        this.output = output;
    }

    public User createUser(BufferedReader br) throws IOException {
        User user = new User();
        String inputString;
        output.output("Enter name: ");
        inputString = br.readLine();
        user.setName(inputString);
        output.output("Enter password: ");
        inputString = br.readLine();
        user.setPassword(inputString);
        user.setBalance(new BigDecimal(10000.00));
        output.output("\nUser: '" + user.getName() + "' was created.");
        logger.info("{} was created.", user.getName());
        return user;
    }

    public boolean authentication(BufferedReader br) throws IOException {
        logger.debug("Begin authentication.");
        String data;
        User userForChecking = new User();
        output.output("\nPlease sign in. \nEnter name: ");
        data = br.readLine();
        userForChecking.setName(data);
        output.output("Enter password: ");
        data = br.readLine();
        userForChecking.setPassword(data);
        if (userService.findUser(userForChecking).isEmpty()) {
            try {
                logger.warn("{} was input by client.", userForChecking.getName());
                throw new UserNotFoundException("User not found");
            } catch (UserNotFoundException e) {
                output.output("You input incorrect user name or password.");
                return false;
            }
        }
        logger.info("Authentication for {} was applied.", userForChecking.getName());
        return true;
    }

    public String createUUID() {
        return UUID.randomUUID().toString();
    }
}
