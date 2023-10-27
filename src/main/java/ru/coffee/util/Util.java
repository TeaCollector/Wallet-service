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
import java.util.Optional;
import java.util.UUID;


/**
 * Util class for additional action with user
 */

public class Util {

    private final UserService<User> userService;
    private final OutputStream<String> output;
    private static final Logger logger = LogManager.getLogger(Util.class.getName());


    public Util(UserService<User> userService, OutputStream<String> output) {
        this.userService = userService;
        this.output = output;
    }

    /**
     * @param br console input
     * @return user which was created
     * @throws IOException
     */
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


    /**
     * @param br console input
     * @return true if user is present in db otherwise false
     * @throws IOException
     */
    public Optional<User> authentication(BufferedReader br) throws IOException {
        logger.debug("Begin authentication.");
        String consoleInput;
        User userForChecking = new User();
        output.output("\nPlease sign in. \nEnter name: ");
        consoleInput = br.readLine();
        userForChecking.setName(consoleInput);
        output.output("Enter password: ");
        consoleInput = br.readLine();
        userForChecking.setPassword(consoleInput);
        if (userService.findUser(userForChecking).isEmpty()) {
            try {
                logger.warn("{} was input by client.", userForChecking.getName());
                throw new UserNotFoundException("User not found");
            } catch (UserNotFoundException e) {
                output.output("You input incorrect user name or password.");
                return Optional.empty();
            }
        }
        logger.info("Authentication for {} was applied.", userForChecking.getName());
        return Optional.of(userService.findUser(userForChecking).get());
    }

    /**
     * Method for creating uniq UUID
     * @return uniq UUID
     */
    public String createUUID() {
        return UUID.randomUUID().toString();
    }


}
