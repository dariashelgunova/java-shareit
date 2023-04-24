package ru.practicum.validation;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Slf4j
public class BookingTimeLimitValidator implements ConstraintValidator<BookingTimeLimit, BookingRequestDto> {
    @Override
    public void initialize(BookingTimeLimit constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingRequestDto booking, ConstraintValidatorContext context) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isEqual(end);
    }
}