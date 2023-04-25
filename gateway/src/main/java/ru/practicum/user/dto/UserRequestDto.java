package ru.practicum.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.validation.Create;
import ru.practicum.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    Long id;
    @NotBlank(groups = {Create.class}, message = "Адрес электронной почты отсутствует. Попробуйте еще раз.")
    @Email(groups = {Create.class, Update.class}, message = "Необходимо ввести электронную почту в соответствующем формате. Например - name@gmail.com")
    String email;
    @NotBlank(groups = {Create.class}, message = "Имя пользователя отсутствует. Попробуйте еще раз.")
    String name;

}
