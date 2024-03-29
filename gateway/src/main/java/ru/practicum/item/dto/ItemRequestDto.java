package ru.practicum.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.validation.Create;
import ru.practicum.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;
    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым. Попробуйте еще раз.")
    String name;
    @NotBlank(groups = {Create.class}, message = "Описание не может быть пустым. Попробуйте еще раз.")
    @Size(groups = {Create.class, Update.class}, max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @NotNull(groups = {Create.class})
    Boolean available;
    Long requestId;
}
