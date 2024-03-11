package com.example.effectivemobile.test.service;

import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.exception.bankaccount.BankAccountException;
import com.example.effectivemobile.test.exception.user.UserNotFoundException;
import com.example.effectivemobile.test.repository.BankAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<BankAccount> findBAllBankAccounts(){return bankAccountRepository.findAll();}

    public void transferMoney(Double money, Optional<User> user){
        user.orElseThrow(()-> new UserNotFoundException("пользователь не найден", user.get().getEmail()));
        var bankAccount = bankAccountRepository.findByUser(user.get());
        bankAccount.get().setDeposit(bankAccount.get().getDeposit()+money);
        bankAccountRepository.save(bankAccount.get());
    }

    public void withdrawMoney(Double money, Optional<User> user){
        user.orElseThrow(()-> new UserNotFoundException("пользователь не найден", user.get().getEmail()));
        var bankAccount = bankAccountRepository.findByUser(user.get()).orElseThrow(()-> new BankAccountException());
        if(bankAccount.getDeposit()-money<0){throw new BankAccountException();}
        bankAccount.setDeposit(bankAccount.getDeposit()-money);
        bankAccountRepository.save(bankAccount);
    }

    public void bankAccountWithdrawfivePercent(BankAccount bankAccount){
        bankAccount.setDeposit(bankAccount.getDeposit()*1.05);
        bankAccountRepository.save(bankAccount);
    }
}
