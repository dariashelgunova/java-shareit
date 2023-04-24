package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = BookingTimeLimitValidator.class)
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BookingTimeLimit {
    String message() default "Старт не может ровняться окончанию бронирования!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

