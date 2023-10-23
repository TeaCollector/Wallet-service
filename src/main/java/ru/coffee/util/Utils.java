package ru.coffee.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.out.OutputStream;
import ru.coffee.service.UserService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;


/**
 * Util class for additional action with user
 */

public class Utils {
    private final UserService userService;
    private final OutputStream<String> output;
    private static final Logger logger = LogManager.getLogger(Utils.class.getName());

    public Utils(UserService userService, OutputStream<String> output) {
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

    public Connection getConnection() throws LiquibaseException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("src/main/resources/db/db.properties")) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String url = properties.getProperty("postgres.url");
        String username = properties.getProperty("postgres.username");
        String password = properties.getProperty("postgres.password");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Database database = null;
        try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.dropAll();
        liquibase.update("");
        return connection;
    }
    /**
     * Method for creating uniq UUID
     * @return uniq UUID
     */
    public String createUUID() {
        return UUID.randomUUID().toString();
    }
}
