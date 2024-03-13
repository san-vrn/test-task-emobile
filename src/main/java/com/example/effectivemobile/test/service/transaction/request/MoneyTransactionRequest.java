package com.example.effectivemobile.test.service.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransactionRequest {
    private String userEmail;
    private Double amountOfMoney;
}
