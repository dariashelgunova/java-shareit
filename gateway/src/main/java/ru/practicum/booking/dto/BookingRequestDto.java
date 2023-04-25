package ru.practicum.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.validation.BookingTimeLimit;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@BookingTimeLimit
public class BookingRequestDto {
    Long id;
    @FutureOrPresent(message = "Дата и время окончания не могут относиться к прошлому. Попробуйте еще раз.")
    LocalDateTime start;
    LocalDateTime end;
    @NotNull
    Long itemId;
}
