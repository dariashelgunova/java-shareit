package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoWithComments {
    Long id;
    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым. Попробуйте еще раз.")
    String name;
    @NotBlank(groups = {Create.class}, message = "Описание не может быть пустым. Попробуйте еще раз.")
    @Size(groups = {Create.class, Update.class}, max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @NotNull(groups = {Create.class})
    Boolean available;
    List<CommentDto> comments;
}

