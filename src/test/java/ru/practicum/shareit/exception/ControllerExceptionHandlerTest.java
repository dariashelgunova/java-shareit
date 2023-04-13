package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerExceptionHandlerTest {

    ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleValidationException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleValidationException(new ValidationException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleOwnValidationException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleOwnValidationException(
                new ru.practicum.shareit.exception.ValidationException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleAccessDeniedException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleAccessDeniedException(new AccessDeniedException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleNotFoundObjectException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleNotFoundObjectException(new NotFoundObjectException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleUniversalException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleUniversalException(new UniversalException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleConstraintViolationException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleConstraintViolationException(
                new ConstraintViolationException(message, null));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleInvalidPageParametersException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleInvalidPageParametersException(
                new InvalidPageParametersException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleUncheckedException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleUncheckedException(new Exception(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleAvailabilityException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleAvailabilityException(new AvailabilityException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleInvalidBookingStateException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleInvalidBookingStateException(new InvalidBookingStateException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleApproveBookingException() {
        String message = "message";

        ResponseEntity<ErrorDetails> response = handler.handleApproveBookingException(new ApproveBookingException(message));

        assertEquals(message, requireNonNull(response.getBody()).getError());
    }

}