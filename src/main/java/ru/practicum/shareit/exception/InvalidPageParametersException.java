package ru.practicum.shareit.exception;

public class InvalidPageParametersException extends RuntimeException {
    public InvalidPageParametersException(String message) {
        super(message);
    }
}
