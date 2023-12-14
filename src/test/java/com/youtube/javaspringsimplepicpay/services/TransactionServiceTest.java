package com.youtube.javaspringsimplepicpay.services;

import com.youtube.javaspringsimplepicpay.domain.user.User;
import com.youtube.javaspringsimplepicpay.domain.user.UserType;
import com.youtube.javaspringsimplepicpay.dtos.TransactionDTO;
import com.youtube.javaspringsimplepicpay.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is OK")
    void createTransactionSuccess() throws Exception {
        User sender = new User(
                1L,
                "John",
                "Doe",
                "99999999901",
                "john_doe@email.com",
                "password",
                new BigDecimal(10),
                UserType.COMMON);

        User receiver = new User(
                2L,
                "Jane",
                "Doe",
                "99999999902",
                "jane_doe@email.com",
                "password",
                new BigDecimal(10),
                UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);

        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1))
                .sendNotification(sender, "Transação realizada com sucesso");

        verify(notificationService, times(1))
                .sendNotification(receiver, "Transação recebida com sucesso");

    }
}