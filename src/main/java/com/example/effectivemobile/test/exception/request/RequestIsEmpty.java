package com.example.effectivemobile.test.exception.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestIsEmpty extends  RuntimeException{
    private String request;
}
