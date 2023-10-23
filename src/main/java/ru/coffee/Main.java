package ru.coffee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.controller.ConsoleController;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.in.InputStream;
import ru.coffee.in.impl.ConsoleInputStream;
import ru.coffee.out.OutputStream;
import ru.coffee.out.impl.ConsoleOutputStream;
import ru.coffee.repository.TransactionRepository;
import ru.coffee.repository.UserRepository;
import ru.coffee.repository.impl.TransactionRepositoryBD;
import ru.coffee.repository.impl.UserRepositoryBD;
import ru.coffee.service.TransactionService;
import ru.coffee.service.UserService;
import ru.coffee.service.impl.TransactionServiceImpl;
import ru.coffee.service.impl.UserServiceImpl;
import ru.coffee.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Main class consist main method it's application's entry point
 */

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Main method enter point to application.
     * @param args nothing
     * @throws IOException
     * @throws UserNotFoundException
     */
    public static void main(String[] args) throws IOException, UserNotFoundException {

        UserRepository userRepository = new UserRepositoryBD();
        UserService userService = new UserServiceImpl(userRepository);
        InputStream<BufferedReader> input = new ConsoleInputStream();
        OutputStream<String> output = new ConsoleOutputStream();
        Util util = new Util(userService, output);
        TransactionRepository transactionRepositoryBD = new TransactionRepositoryBD();
        TransactionService transactionService = new TransactionServiceImpl(transactionRepositoryBD);

        ConsoleController consoleController = new ConsoleController(transactionService, userService,
                input, output, util);

        logger.info("Application run successfully.");
        consoleController.run();

        List<? extends B> list = new ArrayList<>();
        list.add(new C());
        list.add(new A());

    }
}