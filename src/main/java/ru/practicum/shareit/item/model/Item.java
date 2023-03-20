package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id;
    @NotBlank(message = "Имя не может быть пустым. Попробуйте еще раз.")
    String name;
    @NotBlank(message = "Описание не может быть пустым. Попробуйте еще раз.")
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @NotNull
    Boolean available;
    User owner;
    ItemRequest request;
}

