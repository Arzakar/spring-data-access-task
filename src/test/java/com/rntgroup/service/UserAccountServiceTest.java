package com.rntgroup.service;

import com.rntgroup.TestDataUtil;
import com.rntgroup.exception.ValidationException;
import com.rntgroup.model.User;
import com.rntgroup.model.UserAccount;
import com.rntgroup.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void shouldThrowValidationException() {
        UUID userId = UUID.randomUUID();
        User user = TestDataUtil.getRandomUser(userId);
        UserAccount userAccount = new UserAccount().setUser(user);
        ValidationException expectedException = new ValidationException(String.format("У клиента с id = %s уже есть счёт", userId));

        when(userAccountRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(userAccount));

        ValidationException thrown = assertThrows(ValidationException.class, () -> userAccountService.create(userId));
        verify(userAccountRepository).findByUserId(userId);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }
}
