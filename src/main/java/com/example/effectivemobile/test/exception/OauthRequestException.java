package com.example.effectivemobile.test.exception;

public class OauthRequestException extends  RuntimeException{
   private String oauthJwtRequest;
    public OauthRequestException(String message, String jwt) {
        super(message);
        this.oauthJwtRequest=jwt;
    }

    public OauthRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
