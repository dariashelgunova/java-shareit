package ru.practicum.shareit.exception;

import lombok.Value;

@Value
public class ErrorDetails {
    int status;
    String error;
}
