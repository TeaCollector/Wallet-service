package ru.coffee.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffee.domain.User;
import ru.coffee.repository.UserRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void addUser() {
        User user = new User("sasha", "sashok102", new BigDecimal(10000));
//        mock(userService.findUser());
        verify(userService.findUser(user));
        assertEquals(user, 67);
    }

    @Test
    void addMoney() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void findUser() {
    }

    @Test
    void addTransactionId() {
    }

    @Test
    void history() {
    }
}