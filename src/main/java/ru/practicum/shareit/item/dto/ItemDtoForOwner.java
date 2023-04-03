package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.item.comment.dto.CommentSimpleDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoForOwner {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentSimpleDto> comments;
    BookingSimpleDto lastBooking;
    BookingSimpleDto nextBooking;
}




