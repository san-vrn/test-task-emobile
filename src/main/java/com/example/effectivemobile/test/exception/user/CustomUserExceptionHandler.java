package com.example.effectivemobile.test.exception.user;

import com.example.effectivemobile.test.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class CustomUserExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomUserExceptionHandler.class.getName());

    @ExceptionHandler(UserIsExsistsRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> userIsExsistsRequestExceptionHandle(UserIsExsistsRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        CustomException customException = new CustomException(
                e.getMessage()
                , badRequest
                , ZonedDateTime.now(ZoneId.of("UTC+3"))
        );
        logger.info(e.getMessage() + " Пользователь: " + e.getUserEmail());
        return new ResponseEntity<>(customException, badRequest);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> userNotFoundRequestExceptionHandle(UserNotFoundException e){
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        CustomException customException = new CustomException(
                e.getMessage()
                , badRequest
                , ZonedDateTime.now(ZoneId.of("UTC+3"))
        );
        logger.info(e.getMessage() + " Пользователь: " + e.getUserEmail());
        return new ResponseEntity<>(customException, badRequest);
    }


}
