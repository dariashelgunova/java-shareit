package ru.practicum.exception;

import lombok.Value;

@Value
public class FieldError {
    String fieldName;
    String message;
}
