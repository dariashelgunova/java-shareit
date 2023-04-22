package ru.practicum.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {
    Long id;
    @FutureOrPresent(message = "Дата и время окончания не могут относиться к прошлому. Попробуйте еще раз.")
    @NotNull(message = "Дата и время начала не могут быть пустыми. Попробуйте еще раз.")
    LocalDateTime start;
    @Future(message = "Дата и время окончания не могут относиться к прошлому. Попробуйте еще раз.")
    @NotNull(message = "Дата и время окончания не могут быть пустыми. Попробуйте еще раз.")
    LocalDateTime end;
    @NotNull
    Long itemId;
}
