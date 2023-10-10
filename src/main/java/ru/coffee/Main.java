package ru.coffee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.controller.ConsoleController;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.in.InputStream;
import ru.coffee.in.impl.ConsoleInputStream;
import ru.coffee.out.OutputStream;
import ru.coffee.out.impl.ConsoleOutputStream;
import ru.coffee.repository.UserRepository;
import ru.coffee.repository.impl.UserRepositoryImpl;
import ru.coffee.service.UserService;
import ru.coffee.service.impl.UserServiceImpl;
import ru.coffee.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, UserNotFoundException {
        InputStream<BufferedReader> input = new ConsoleInputStream();
        OutputStream output = new ConsoleOutputStream();
        UserRepository userRepository = new UserRepositoryImpl(output);
        UserService userService = new UserServiceImpl(userRepository);
        Utils tokenCreator = new Utils(userService, output);
        ConsoleController consoleController = new ConsoleController(userService,
                input, output, tokenCreator);

        logger.info("Application run successfully.");
        consoleController.run();
    }
}