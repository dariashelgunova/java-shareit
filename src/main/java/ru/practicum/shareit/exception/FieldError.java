package ru.practicum.shareit.exception;

import lombok.Value;

@Value
public class FieldError {
    String fieldName;
    String message;
}
