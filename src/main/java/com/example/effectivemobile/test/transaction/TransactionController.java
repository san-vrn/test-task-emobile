package com.example.effectivemobile.test.transaction;

import com.example.effectivemobile.test.authentication.request.RegisterRequest;
import com.example.effectivemobile.test.service.TransactionService;
import com.example.effectivemobile.test.transaction.request.TransactionRequest;
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
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> transactionToUser( @RequestBody TransactionRequest transactionRequest){
            return ResponseEntity.ok(transactionService.register(request));
    }
}
