package com.rntgroup.service;

import com.rntgroup.exception.ValidationException;
import com.rntgroup.model.UserAccount;
import com.rntgroup.repository.UserAccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAccountService {

    UserAccountRepository userAccountRepository;
    UserService userService;

    public UserAccount create(UUID userId) {
        if (userAccountRepository.findByUserId(userId).isEmpty()) {
            throw new ValidationException(String.format("У клиента с id = %s уже есть счёт", userId));
        }

        UserAccount userAccount = new UserAccount()
                .setUser(userService.findById(userId))
                .setAmount(BigDecimal.ZERO);
        return userAccountRepository.save(userAccount);
    }

    public UserAccount replenish(UUID userId, BigDecimal amount) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow();
        BigDecimal newAmount = userAccount.getAmount().add(amount);

        return userAccountRepository.save(userAccount.setAmount(newAmount));
    }

    public UserAccount withdraw(UUID userId, BigDecimal amount) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow();
        BigDecimal newAmount = userAccount.getAmount().subtract(amount);

        return userAccountRepository.save(userAccount.setAmount(newAmount));
    }
}
