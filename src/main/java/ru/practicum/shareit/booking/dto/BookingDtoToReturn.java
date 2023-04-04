package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.user.dto.UserSimpleDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoToReturn {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemSimpleDto item;
    UserSimpleDto booker;
    Status status;
}
