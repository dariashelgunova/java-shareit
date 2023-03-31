package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @FutureOrPresent(message = "Дата и время окончания не могут относиться к прошлому. Попробуйте еще раз.")
    @NotNull(message = "Дата и время начала не могут быть пустыми. Попробуйте еще раз.")
    //@JsonFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    LocalDateTime start;
    @FutureOrPresent(message = "Дата и время окончания не могут относиться к прошлому. Попробуйте еще раз.")
    @NotNull(message = "Дата и время окончания не могут быть пустыми. Попробуйте еще раз.")
    //@JsonFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    LocalDateTime end;
    Long itemId;
    Long bookerId;
    Status status;
}
