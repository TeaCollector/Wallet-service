package ru.coffee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.config.DBConnectionProvider;
import ru.coffee.controller.ConsoleController;
import ru.coffee.domain.User;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Main class consist main method it's application's entry point
 */

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Main method enter point to application.
     *
     * @param args nothing
     * @throws IOException
     * @throws UserNotFoundException
     */
    public static void main(String[] args) throws IOException, UserNotFoundException {
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(new FileInputStream("src/main/resources/db/db.properties"))) {
            properties.load(in);
        }
        DBConnectionProvider provider = new DBConnectionProvider(properties.getProperty("postgres.url"),
                properties.getProperty("postgres.username"), properties.getProperty("postgres.password"));

        InputStream<BufferedReader> input = new ConsoleInputStream();
        OutputStream<String> output = new ConsoleOutputStream();
        UserRepository<User> userRepository = new UserRepositoryBD(output, provider);
        UserService<User> userService = new UserServiceImpl(userRepository);
        Util util = new Util(userService, output);
        TransactionRepository transactionRepositoryBD = new TransactionRepositoryBD(provider);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepositoryBD);

        ConsoleController consoleController = new ConsoleController(transactionService, userService,
                input, output, util);

        logger.info("Application run successfully.");
        consoleController.run();

    }
}