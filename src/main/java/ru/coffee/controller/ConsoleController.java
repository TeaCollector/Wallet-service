package ru.coffee.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.in.InputStream;
import ru.coffee.out.OutputStream;
import ru.coffee.service.UserService;
import ru.coffee.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConsoleController {
    private final UserService userService;
    private final InputStream<BufferedReader> input;
    private final OutputStream output;
    private final Utils utils;
    private static final Logger logger = LogManager.getLogger(ConsoleController.class.getName());

    public ConsoleController(UserService userService,
                             InputStream<BufferedReader> input,
                             OutputStream output,
                             Utils utils) {
        this.userService = userService;
        this.input = input;
        this.output = output;
        this.utils = utils;
    }

    public void run() throws IOException, UserNotFoundException {
        output.output("\nWelcome to our application.\nSign up input: '1' for sign in: '2': ");
        try (BufferedReader br = input.input()) {
            User user = new User();
            int option = Integer.parseInt(br.readLine());
            if (option == 2) {
                utils.authentication(br);
            } else {
                user = utils.createUser(br);
                userService.addUser(user);
            }
            while (true) {
                if (utils.authentication(br)) {
                    break;
                }
            }

            output.output("\nWelcome to our application!\n" +
                          "You balance now: " + user.getBalance() + "$\n" +
                          "Here you can add money to your account or withdraw it. \n" +
                          "for example input: '+1000' this mean you add 1000$ to yourself.  \n" +
                          "Or input: '-300' you withdraw 300$" +
                          "If you want to exit from account print: 'exit'\n" +
                          "For quit from application print 'quit'" +
                          "Have fun! \n" +
                          "\n");

            usersAction(br, user);
        }
    }

    private void usersAction(BufferedReader br, User user) throws IOException, UserNotFoundException {
        while (true) {
            output.output("Input here: ");
            String action = br.readLine();
            if (action.charAt(0) == '+') {
                String transactionId = utils.createUUID();
                userService.addTransactionId(transactionId);
                userService.addMoney(user, transactionId, new BigDecimal(action));
                output.output("\nYour balance: " + user.getBalance().setScale(4, RoundingMode.CEILING) + "\n");
            } else if (action.charAt(0) == '-') {
                String transactionId = utils.createUUID();
                userService.addTransactionId(transactionId);
                userService.withdraw(user, transactionId, new BigDecimal(action));
                output.output("\nYour balance: " + user.getBalance().setScale(4, RoundingMode.CEILING) + "\n");
            } else if (action.equals("history")) {
                userService.history(user);
            } else if (action.equals("exit")) {
                logger.info("{} exit from account", user);
                output.output("\nYou exit from account.");
                run();
            } else if (action.equals("quit")) {
                logger.info("Exit from application.");
                return;
            }
        }
    }
}