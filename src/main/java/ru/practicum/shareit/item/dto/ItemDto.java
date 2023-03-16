package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank(message = "Имя не может быть пустым. Попробуйте еще раз.")
    String name;
    @NotBlank(message = "Описание не может быть пустым. Попробуйте еще раз.")
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @NotNull
    Boolean available;
}
