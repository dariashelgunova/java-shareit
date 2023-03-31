package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoToReturn {
    Long id;
    //@JsonFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    LocalDateTime start;
    //@JsonFormat(pattern = "YYYY-mm-dd'T'hh:MM:ss")
    LocalDateTime end;
    Item item;
    User booker;
    Status status;
}

