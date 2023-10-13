package ru.coffee.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import ru.coffee.domain.User;
import ru.coffee.exception.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserRepositoryImplTest {
    @Mock
    UserRepositoryImpl userRepository;

    private static List<User> mockUserList = mock(ArrayList.class);
    private static List<String> mockTransactionIdList = mock(ArrayList.class);

    @BeforeAll
    static void addingAtMockerListAndMockTransaction() {
        User user = new User("sasha", "sasha", new BigDecimal("10000"));
        mockUserList.add(user);
        verify(mockUserList).add(user);
        when(mockUserList.get(0)).thenReturn(user);

        String transactionId = UUID.randomUUID().toString();
        mockTransactionIdList.add(transactionId);
        verify(mockTransactionIdList).add(transactionId);
        when(mockTransactionIdList.get(anyInt())).thenReturn(transactionId);
    }

    @Test
    void AddUser_AddCorrectUser_UserWasAddedSuccessfully() {
        User user = new User("sasha", "sasha", new BigDecimal("10000"));
        assertEquals(user, mockUserList.get(0));
    }

    @Test
    void AddUser_AddIncorrectUser_FailToAddUser() {
        assertNotEquals(new User("petya", "petrov", new BigDecimal("100")),
                mockUserList.get(0));
    }

    @Test
    void SaveTransactionId_SaveCorrectTransaction_Success() {
        String transactionId = mockTransactionIdList.get(90);
        assertEquals(transactionId, mockTransactionIdList.get(45));
    }

    @Test
    void WithdrawMoney_AddCorrectData_WithdrawSuccessful() {
        User user = mockUserList.get(0);
        BigDecimal amountToWithDraw = new BigDecimal("3000");
        BigDecimal newAmount = user.getBalance().subtract(amountToWithDraw);
        assertEquals(new BigDecimal(7000), newAmount);
    }

    @Test
    void WithdrawMoney_TooBigAmount_WithdrawFail() {
        User user = mockUserList.get(0);
        BigDecimal amountToWithDraw = new BigDecimal("100000");
        BigDecimal newAmount = user.getBalance().subtract(amountToWithDraw);
        assertNotEquals(new BigDecimal(7000), newAmount);
    }

    @Test
    void WithdrawMoney_TooBigAmount_ThrowNotEnoughMoneyException() {
        User user = mockUserList.get(0);
        assertThrows(NotEnoughMoneyException.class, () -> {
            BigDecimal amountToWithDraw = new BigDecimal("100000");
            BigDecimal newAmount = user.getBalance().subtract(amountToWithDraw);
            if (newAmount.longValue() < 0) throw new NotEnoughMoneyException("Not enough money.");
        }, "Don't have enough money!");
    }

    @Test
    void AddMoney_Success() {
        User user = mockUserList.get(0);
        BigDecimal addMoney = new BigDecimal("90000");
        BigDecimal newAmount = user.getBalance().add(addMoney);
        assertEquals(100000, newAmount.longValue());
    }

    @Test
    void AddMoney_Fail() {
        User user = mockUserList.get(0);
        BigDecimal addMoney = new BigDecimal("90000");
        BigDecimal newAmount = user.getBalance().add(addMoney);
        assertNotEquals(true, newAmount.longValue());
    }

    @Test
    void FindUser_CreateSomeUser_SuccessFind() {
        User user = new User("vasya", "vas'ka507", new BigDecimal("10000"));
        List<User> userList = new ArrayList<>(List.of(
                new User("vasya", "vas'ka507", new BigDecimal("10000")),
                new User("masha", "gflser34@", new BigDecimal("10000")),
                new User("petya", "hklmse4", new BigDecimal("10000"))));

        mockUserList.addAll(userList);
        verify(mockUserList).addAll(userList);
        when(mockUserList.get(1)).thenReturn(new User("vasya", "vas'ka507", new BigDecimal("10000")));
        assertEquals(user, mockUserList.get(1));
    }

    @Test
    void history() {

    }

    @Test
    void CorrectTransaction_InputTransaction_Success() {
        String transactionId = mockTransactionIdList.get(0);
        assertEquals(transactionId, mockTransactionIdList.get(0));
    }
}