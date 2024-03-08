package com.example.effectivemobile.test.repository;

import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import com.example.effectivemobile.test.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

    Optional<BankAccount> findByUser(User user);
}
