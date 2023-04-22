package ru.practicum.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserSimpleDto;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    @Test
    void fromUserRequestDtoTest() {
        UserRequestDto dto = new UserRequestDto(1L, "yandex@yandex.ru", "name");
        User user = UserMapper.fromUserRequestDto(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getName(), user.getName());

        dto = null;
        assertNull(UserMapper.fromUserRequestDto(dto));
    }

    @Test
    void toUserRequestDtoTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        UserRequestDto dto = UserMapper.toUserRequestDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getName(), dto.getName());

        user = null;
        assertNull(UserMapper.toUserRequestDto(user));
    }

    @Test
    void toUserRequestDtoListTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        List<User> users = List.of(user);

        List<UserRequestDto> dtos = UserMapper.toUserRequestDtoList(users);

        assertEquals(users.size(), dtos.size());

        users = null;
        assertNull(UserMapper.toUserRequestDtoList(users));
    }

    @Test
    void toUserSimpleDtoTest() {
        User user = new User(1L, "yandex@yandex.ru", "name");
        UserSimpleDto dto = UserMapper.toUserSimpleDto(user);

        assertEquals(user.getId(), dto.getId());

        user = null;
        assertNull(UserMapper.toUserRequestDto(user));
    }

}