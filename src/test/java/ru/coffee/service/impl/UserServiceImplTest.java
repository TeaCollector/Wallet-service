package ru.coffee.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.coffee.domain.User;
import ru.coffee.exception.UserNotFoundException;
import ru.coffee.repository.UserRepository;
import ru.coffee.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private List<User> mockUserList;
    private UserService userService;
    private User user;

    @SneakyThrows
    @BeforeEach
    public void init() {
        userService = mock(UserServiceImpl.class);
        mockUserList = mock(ArrayList.class);
        mockUserList.add(user);
        verify(mockUserList).add(user);
        user = new User("sasha", "sasha", new BigDecimal(10_000));
        when(userService.findUser(user)).thenReturn(Optional.of(user));

    }

    @Test
    void addUser() {
        doThrow(new RuntimeException()).when(userService).addUser(user);
        doAnswer(invocation -> "All fine!").when(userService).addUser(user);
    }

    @SneakyThrows
    @Test
    void AddMoney_CorrectInputData_Success() {
        try {
            doThrow(UserNotFoundException.class).when(userService).addMoney(any(), anyString(), any());

        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void captureInputData_CorrectInputData_AllRight() {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.findUser(user);
        verify(userService).findUser(userArgumentCaptor.capture());
        assertEquals(1, userArgumentCaptor.getAllValues().size());
        User capturedUser = userArgumentCaptor.getValue();
        assertEquals("sasha", capturedUser.getName());
        assertEquals("sasha", capturedUser.getPassword());
        assertEquals(10_000, capturedUser.getBalance().longValue());
    }

    @Test
    void Withdraw_Success() {
    }

    @Test
    void FindUser_CorrectInputData_Success() {
        assertEquals(Optional.of(user), userService.findUser(user));
        verify(userService).findUser(user);
    }

    @Test
    void AddTransactionId_Success() {
        doAnswer(inv -> "Hello!").when(userService).addTransactionId(any());
    }

    @Test
    void ShowHistory_Fine() {
        System.out.println(mockUserList.get(0));
    }
}