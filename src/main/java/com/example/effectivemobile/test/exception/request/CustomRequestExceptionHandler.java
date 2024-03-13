package com.example.effectivemobile.test.exception.request;

import com.example.effectivemobile.test.exception.CustomException;
import com.example.effectivemobile.test.exception.user.UserIsExsistsRequestException;
import com.example.effectivemobile.test.exception.user.UserNotFoundException;
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
public class CustomRequestExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomRequestExceptionHandler.class.getName());

    @ExceptionHandler(RequestIsEmpty.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> requestIsEmptyExceptionHandle(RequestIsEmpty e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        CustomException customException = new CustomException(
                e.getMessage()
                , badRequest
                , ZonedDateTime.now(ZoneId.of("UTC+3"))
        );
        logger.info(e.getRequest() + " Ошибка " + e.getMessage());
        return new ResponseEntity<>(customException, badRequest);
    }
}
