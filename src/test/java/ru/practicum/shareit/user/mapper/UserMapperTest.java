package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserSimpleDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.user.mapper.UserMapper.*;

class UserMapperTest {

    @Test
    void fromUserRequestDtoTest() {
        UserRequestDto dto = new UserRequestDto(1L, "yandex@yandex.ru", "name");
        User user = fromUserRequestDto(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getName(), user.getName());

        dto = null;
        assertNull(fromUserRequestDto(dto));
    }

    @Test
    void toUserRequestDtoTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        UserRequestDto dto = toUserRequestDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getName(), dto.getName());

        user = null;
        assertNull(toUserRequestDto(user));
    }

    @Test
    void toUserRequestDtoListTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        List<User> users = List.of(user);

        List<UserRequestDto> dtos = toUserRequestDtoList(users);

        assertEquals(users.size(), dtos.size());

        users = null;
        assertNull(toUserRequestDtoList(users));
    }

    @Test
    void toUserSimpleDtoTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        UserSimpleDto dto = toUserSimpleDto(user);

        assertEquals(user.getId(), dto.getId());

        user = null;
        assertNull(toUserRequestDto(user));
    }

}