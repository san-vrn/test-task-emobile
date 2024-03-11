package com.example.effectivemobile.test.service;

import com.example.effectivemobile.test.exception.user.UserNotFoundException;
import com.example.effectivemobile.test.transaction.request.MoneyTransactionRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MoneyTransactionService {

    private final UserService userService;
    private final AuditorAware<Long> auditorAware;
    private final BankAccountService bankAccountService;

    @Autowired
    public MoneyTransactionService(UserService userService, AuditorAware<Long> auditorAware, BankAccountService bankAccountService) {
        this.userService = userService;
        this.auditorAware = auditorAware;
        this.bankAccountService = bankAccountService;
    }


    public String moneyTransaction(MoneyTransactionRequest moneyTransactionRequest) {
        try {
            var userFrom = userService.findByUserId(auditorAware.getCurrentAuditor().get());
            var userTo = userService.findByUserEmail(moneyTransactionRequest.getUserEmail());

            userTo.orElseThrow(() -> new UserNotFoundException(moneyTransactionRequest.getUserEmail(),
                    "Пользователь с email " + moneyTransactionRequest.getUserEmail() + " не найден"));

            bankAccountService.withdrawMoney(moneyTransactionRequest.getAmountOfMoney(), userFrom);
            bankAccountService.transferMoney(moneyTransactionRequest.getAmountOfMoney(), userTo);
            return "Транзакция для пользователя " + userTo.get().getEmail() + " успешна";
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
