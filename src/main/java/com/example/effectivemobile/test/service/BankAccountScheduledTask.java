package com.example.effectivemobile.test.service;

import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BankAccountScheduledTask {

    private final BankAccountService bankAccountService;

    public BankAccountScheduledTask(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Scheduled(fixedRate = 60000)
    public void updateDepositAllBankAccount(){
        var allBankAccounts = bankAccountService.findBAllBankAccounts();
        for(BankAccount bankAccount: allBankAccounts){
            if(bankAccount.getDeposit() *1.05 <bankAccount.getMaxDeposit()){
                bankAccountService.bankAccountWithdrawfivePercent(bankAccount);
            }
        }

    }
}
