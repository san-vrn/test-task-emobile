package com.example.effectivemobile.test.configuration;

import com.example.effectivemobile.test.service.bankaccount.BankAccountScheduledTask;
import com.example.effectivemobile.test.service.bankaccount.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    public BankAccountScheduledTask bankAccountScheduledTask(@Autowired BankAccountService bankAccountService) {
        return new BankAccountScheduledTask(bankAccountService);
    }
}