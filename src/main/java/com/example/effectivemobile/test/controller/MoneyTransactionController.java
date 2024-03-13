package com.example.effectivemobile.test.controller;

import com.example.effectivemobile.test.service.transaction.MoneyTransactionService;
import com.example.effectivemobile.test.service.transaction.request.MoneyTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class MoneyTransactionController {

    private MoneyTransactionService transactionService;

    @Autowired
    public MoneyTransactionController(MoneyTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> transactionToUser( @RequestBody MoneyTransactionRequest transactionRequest){
            return ResponseEntity.ok(transactionService.moneyTransaction(transactionRequest));
    }
}
