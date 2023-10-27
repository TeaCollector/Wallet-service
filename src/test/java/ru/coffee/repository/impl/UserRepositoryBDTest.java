package ru.coffee.repository.impl;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.coffee.config.DBConnectionProvider;
import ru.coffee.domain.User;
import ru.coffee.out.OutputStream;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryBDTest {
    @Mock
    OutputStream<String> outputStream;
    @InjectMocks
    UserRepositoryBD userRepositoryBD;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.4");
    User user;

    @BeforeAll
    static void init() {
        postgres.start();
    }
    @AfterAll
    static void close() {
        postgres.close();
    }
    @BeforeEach
    void setUp() {
        outputStream = mock(OutputStream.class);
        doNothing().when(outputStream).output(any());
        user = User.builder().name("sasha").password("sasha").balance(new BigDecimal("400.0000")).build();
        userRepositoryBD = new UserRepositoryBD(outputStream,
                new DBConnectionProvider(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Adding user - success")
    void addUser() {
        User addingUser = User.
                builder().
                name("vasya").
                password("12345").
                balance(new BigDecimal("10000")).
                build();

        userRepositoryBD.addUser(addingUser);

        Optional<User> user = userRepositoryBD.findUser(addingUser);
        assertEquals("vasya", user.get().getName());
        assertEquals("12345", user.get().getPassword());
    }

    @Test
    @DisplayName("Add money to user - success.")
    void addMoney() {
        long userMoney = userRepositoryBD.
                addMoney(user, new BigDecimal(100)).getBalance().longValue();
        assertEquals(500, userMoney);
    }

    @Test
    @DisplayName("Withdraw money - success")
    void withdraw() {
        long userMoney = 0;

        userMoney = userRepositoryBD.withdraw(user, new BigDecimal("100.0000")).getBalance().longValue();

        assertEquals(300, userMoney);
    }

    @Test
    @DisplayName("Input incorrect value - not throw exception")
    void throwException() {
        assertDoesNotThrow(() -> userRepositoryBD.withdraw(user, new BigDecimal(-1000)));
    }

    @Test
    void findUser() {
        User userForCheck = userRepositoryBD.findUser(user).get();
        assertEquals("sasha", userForCheck.getName());
        assertEquals("sasha", userForCheck.getPassword());
    }

    @Test
    void history() {
    }

}