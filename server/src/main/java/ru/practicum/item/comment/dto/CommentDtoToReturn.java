package ru.practicum.item.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.dto.ItemSimpleDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoToReturn {
    Long id;
    String text;
    ItemSimpleDto item;
    String authorName;
    LocalDateTime created;
}
