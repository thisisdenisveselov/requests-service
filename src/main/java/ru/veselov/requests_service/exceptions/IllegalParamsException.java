package ru.veselov.requests_service.exceptions;

public class IllegalParamsException extends RuntimeException {
    public IllegalParamsException(String message) {
        super(message);
    }
}
