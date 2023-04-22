package ru.practicum.exception;

public class InvalidPageParametersException extends RuntimeException {
    public InvalidPageParametersException(String message) {
        super(message);
    }
}
