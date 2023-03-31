package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.InvalidBookingStateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State findByValueOrThrowException(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidBookingStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
