package com.example.effectivemobile.test.service;

import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.repository.BankAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Optional<BankAccount> findByUserId(User user){return bankAccountRepository.findByUser(user);}

}
