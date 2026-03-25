package ru.valerii.NauJava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TransactionNotFoundException.class, AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RuntimeException e) {
        return ApiError.create(e);
    }

    @ExceptionHandler(TransactionValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(TransactionValidationException e) {
        return ApiError.create(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAll(Exception e) {
        return ApiError.create("Произошла непредвиденная ошибка: " + e.getMessage());
    }
}