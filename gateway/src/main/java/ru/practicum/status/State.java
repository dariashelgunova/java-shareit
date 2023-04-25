package ru.practicum.status;


import ru.practicum.exception.InvalidBookingStateException;

import java.util.Arrays;
import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State findByValueOrThrowException(String value) {
        Optional<State> state = Arrays.stream(values())
                .filter(c -> c.name().equalsIgnoreCase(value))
                .findFirst();
        return state.orElseThrow(() -> new InvalidBookingStateException("Unknown state: UNSUPPORTED_STATUS"));
    }
}
