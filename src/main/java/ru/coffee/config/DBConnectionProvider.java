package ru.coffee.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionProvider {
    private String url;
    private String username;
    private String password;

    public DBConnectionProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private static boolean activeLiquibase = false;

    public Connection getConnection() throws LiquibaseException {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            if (!activeLiquibase) {
                startLiquibase();
                activeLiquibase = true;
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void startLiquibase() throws LiquibaseException {
        Database database;
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.dropAll();
        liquibase.update();
    }
}